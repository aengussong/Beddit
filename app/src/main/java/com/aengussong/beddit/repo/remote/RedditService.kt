package com.aengussong.beddit.repo.remote

import com.aengussong.beddit.model.RedditResponse
import com.aengussong.beddit.util.LOAD_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("/best.json")
    suspend fun getBest(
        @Query("limit") loadSize: Int = LOAD_SIZE,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditResponse>
}