package com.aengussong.beddit.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingData(
    val data: LiveData<PagedList<RedditPost>>,
    val requestState: LiveData<State>
)