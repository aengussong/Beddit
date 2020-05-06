package com.aengussong.beddit.util

import android.webkit.URLUtil
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.RedditResponse
import retrofit2.Response

fun Response<RedditResponse>.getPosts(): List<RedditPost> =
    body()?.data?.children?.map { it.data } ?: emptyList()

fun String.isValidUrl(): Boolean = URLUtil.isValidUrl(this)