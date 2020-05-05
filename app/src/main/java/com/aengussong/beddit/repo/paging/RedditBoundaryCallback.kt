package com.aengussong.beddit.repo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.RedditResponse
import com.aengussong.beddit.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


class RedditBoundaryCallback(
    private val coroutineContext: CoroutineContext,
    private val networkPageSize: Int,
    val handleResponse: (List<RedditPost>) -> Unit,
    val onLoadData: suspend (Int, String?) -> Response<RedditResponse>
) : PagedList.BoundaryCallback<RedditPost>() {
    val requestState: MutableLiveData<State> = MutableLiveData()

    override fun onZeroItemsLoaded() = loadData(null)
    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) = loadData(itemAtEnd.name)

    private fun loadData(lastItemName:String?){
        CoroutineScope(this.coroutineContext).launch {
            val newData = onLoadData(networkPageSize, lastItemName)

            if (!newData.isSuccessful) {
                requestState.postValue(State.RequestError(newData.message()))
                return@launch
            }

            handleResponse(newData.body()
                ?.data
                ?.children
                ?.map {
                    it.data
                } ?: emptyList())
        }
    }
}