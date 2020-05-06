package com.aengussong.beddit.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class RedditData(
    val data: LiveData<PagedList<RedditPost>>,
    val requestState: LiveData<State>,
    val refresh: () -> Unit,
    val refreshState: LiveData<State>
)