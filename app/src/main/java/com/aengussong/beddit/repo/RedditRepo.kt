package com.aengussong.beddit.repo

import androidx.paging.LivePagedListBuilder
import com.aengussong.beddit.model.PagingData
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.repo.local.dao.PostDao
import com.aengussong.beddit.repo.paging.RedditBoundaryCallback
import com.aengussong.beddit.repo.remote.RedditService
import kotlin.coroutines.CoroutineContext

class RedditRepo(
    private val local: PostDao,
    private val remote: RedditService,
    private val pagedListBuilder: LivePagedListBuilder<Int, RedditPost>
) {
    fun loadData(coroutineContext: CoroutineContext): PagingData {
        val boundaryCallback = RedditBoundaryCallback(
            coroutineContext,
            25,
            handleResponse = ::insertToDb,
            onLoadData = ::loadFromRemote
        )

        val livePagedData = pagedListBuilder.setBoundaryCallback(boundaryCallback).build()
        val requestState = boundaryCallback.requestState

        return PagingData(livePagedData, requestState)
    }

    private fun insertToDb(posts: List<RedditPost>) = local.addPosts(posts)

    private suspend fun loadFromRemote(
        loadSize: Int,
        lastItemName: String? = null
    ) = remote.getBest(loadSize = loadSize, after = lastItemName)
}