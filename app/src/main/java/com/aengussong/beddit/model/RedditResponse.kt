package com.aengussong.beddit.model

import android.webkit.URLUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RedditResponse(val data: RedditListing)

data class RedditListing(
    val dist: Int,
    val children: List<RedditPostContainer>,
    val after: String?,
    val before: String?
)

data class RedditPostContainer(val data:RedditPost)

//todo add mappers? nah too lazy
@Entity
data class RedditPost(@PrimaryKey val name:String, val title:String, val selftext:String, val thumbnail:String){

    class DiffUtilCallBack : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.selftext == newItem.selftext
                    && oldItem.thumbnail == newItem.thumbnail
        }
    }
}