package com.aengussong.beddit.repo.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aengussong.beddit.model.RedditPost

@Dao
interface PostDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPosts(posts:List<RedditPost>)

    @Query("SELECT * FROM redditpost")
    fun getPosts(): DataSource.Factory<Int, RedditPost>

}