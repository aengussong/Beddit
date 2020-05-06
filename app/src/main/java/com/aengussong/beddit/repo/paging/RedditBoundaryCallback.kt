package com.aengussong.beddit.repo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.RedditResponse
import com.aengussong.beddit.util.getPosts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext


class RedditBoundaryCallback(
    private val coroutineContext: CoroutineContext,
    private val networkPageSize: Int,
    val handleResponse: (List<RedditPost>) -> Unit,
    val onLoadData: suspend (Int, String?) -> Response<RedditResponse>
) : PagedList.BoundaryCallback<RedditPost>() {
    val requestError: MutableLiveData<ErrorRequest> = MutableLiveData()

    override fun onZeroItemsLoaded() = loadData(null)
    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) = onItemAtEndLoaded(itemAtEnd.name)
    fun onItemAtEndLoaded(lastItemName: String) = loadData(lastItemName)

    private fun loadData(lastItemName: String?) {
        CoroutineScope(this.coroutineContext).launch {
            try {
                val newData = onLoadData(networkPageSize, lastItemName)

                if (!newData.isSuccessful) {
                    handleError(lastItemName)
                    return@launch
                }

                handleResponse(newData.getPosts())
            } catch (ex: IOException) {
                handleError(lastItemName)
            }
        }
    }

    private fun handleError(lastItemName: String?) {
        val errorRequest = lastItemName?.let {
            ErrorRequest.OnItemAtEndError(lastItemName)
        } ?: ErrorRequest.OnZeroItemsError
        requestError.postValue(errorRequest)
    }

    sealed class ErrorRequest {
        object OnZeroItemsError : ErrorRequest()
        data class OnItemAtEndError(val lastItemName: String) : ErrorRequest()
    }
}