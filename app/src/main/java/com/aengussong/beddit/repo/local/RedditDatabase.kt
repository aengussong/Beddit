package com.aengussong.beddit.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.repo.local.dao.PostDao

@Database(entities = [RedditPost::class], version = 1)
abstract class RedditDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}