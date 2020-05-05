package com.aengussong.beddit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.State
import com.aengussong.beddit.repo.RedditRepo
import kotlinx.coroutines.Dispatchers

class MainViewModel(repo: RedditRepo) : ViewModel() {

    val bestLiveData: LiveData<PagedList<RedditPost>> = repo.loadData(Dispatchers.IO).data

    val requestErrors: LiveData<State> = repo.loadData(Dispatchers.IO).requestState
}