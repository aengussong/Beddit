package com.aengussong.beddit.repo.remote

import com.aengussong.beddit.model.RedditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("/best.json")
    suspend fun getBest(
        @Query("limit") loadSize: Int = 30,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditResponse>
}