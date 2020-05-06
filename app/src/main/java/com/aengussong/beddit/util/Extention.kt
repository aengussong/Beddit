package com.aengussong.beddit.util

import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.RedditResponse
import retrofit2.Response

fun Response<RedditResponse>.getPosts(): List<RedditPost> =
    body()?.data?.children?.map { it.data } ?: emptyList()