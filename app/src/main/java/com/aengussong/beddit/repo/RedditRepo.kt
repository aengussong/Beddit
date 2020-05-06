package com.aengussong.beddit.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.aengussong.beddit.model.RedditData
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.State
import com.aengussong.beddit.repo.local.dao.PostDao
import com.aengussong.beddit.repo.paging.RedditBoundaryCallback
import com.aengussong.beddit.repo.remote.RedditService
import com.aengussong.beddit.util.LOAD_SIZE
import com.aengussong.beddit.util.getPosts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RedditRepo(
    private val local: PostDao,
    private val remote: RedditService,
    private val pagedListBuilder: LivePagedListBuilder<Int, RedditPost>
) {

    fun loadData(coroutineContext: CoroutineContext): RedditData {
        val boundaryCallback = RedditBoundaryCallback(
            coroutineContext,
            LOAD_SIZE,
            handleResponse = ::insertToDb,
            onLoadData = ::loadFromRemote
        )

        val livePagedData = pagedListBuilder.setBoundaryCallback(boundaryCallback).build()

        val retry: LiveData<() -> Unit> =
            Transformations.map(boundaryCallback.requestError) { errorRequest ->
                return@map when (errorRequest) {
                    is RedditBoundaryCallback.ErrorRequest.OnZeroItemsError -> {
                        { boundaryCallback.onZeroItemsLoaded() }
                    }
                    is RedditBoundaryCallback.ErrorRequest.OnItemAtEndError -> {
                        val lastName = errorRequest.lastItemName
                        {
                            boundaryCallback.onItemAtEndLoaded(lastName)
                        }
                    }
                }
            }

        val refreshTrigger = MutableLiveData<Unit>()

        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(coroutineContext)
        }

        return RedditData(
            livePagedData,
            refresh = { refreshTrigger.value = null },
            refreshState = refreshState,
            requestError = retry
        )

    }

    private fun refresh(coroutineContext: CoroutineContext): LiveData<State> {
        val refreshState = MutableLiveData<State>()

        CoroutineScope(coroutineContext).launch {
            refreshState.postValue(State.LOADING)
            val response = loadFromRemote()
            if (!response.isSuccessful) {
                refreshState.postValue(State.ERROR)
                return@launch
            }
            refreshState.postValue(State.SUCCESS)
            dropDb()
            insertToDb(response.getPosts())
        }

        return refreshState
    }

    private fun insertToDb(posts: List<RedditPost>) = local.addPosts(posts)

    private fun dropDb() = local.clear()

    private suspend fun loadFromRemote(
        loadSize: Int = LOAD_SIZE,
        lastItemName: String? = null
    ) = remote.getBest(loadSize = loadSize, after = lastItemName)
}