package com.aengussong.beddit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.State
import com.aengussong.beddit.repo.RedditRepo
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repo: RedditRepo) : ViewModel() {

    private val redditData = repo.loadData(Dispatchers.IO)

    val postsLiveData: LiveData<PagedList<RedditPost>> = redditData.data

    val refreshState:LiveData<State> = redditData.refreshState

    val requestError = redditData.requestError

    fun refresh() = redditData.refresh.invoke()
}