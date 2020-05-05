package com.aengussong.beddit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aengussong.beddit.R
import com.aengussong.beddit.model.RedditPost
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_reddit_post.view.*

class RedditPostAdapter :
    PagedListAdapter<RedditPost, RedditPostAdapter.PostViewHolder>(RedditPost.DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reddit_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bindPost(it) }
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPost(post: RedditPost) {
            itemView.apply {
                title.text = post.title
                description.text = post.selftext
                Glide.with(itemView).apply {
                    when (post.isThumbnailAvailable()) {
                        true -> load(post.thumbnail).into(thumbnail)
                        false -> {
                            clear(thumbnail)
                            thumbnail.visibility = View.GONE
                        }
                    }
                }
            }

        }
    }
}
