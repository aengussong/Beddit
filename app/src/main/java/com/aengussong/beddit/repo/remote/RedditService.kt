package com.aengussong.beddit.repo.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("/best.json")
    suspend fun getBest(@Query("after") after: String? = null)
}