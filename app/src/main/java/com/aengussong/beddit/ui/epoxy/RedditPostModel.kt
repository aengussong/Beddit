package com.aengussong.beddit.ui.epoxy

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aengussong.beddit.R
import com.aengussong.beddit.util.isValidUrl
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_reddit_post.view.*

@EpoxyModelClass(layout = R.layout.item_reddit_post)
abstract class RedditPostModel : EpoxyModelWithHolder<RedditPostModel.RedditPostHolder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var description: String

    @EpoxyAttribute
    lateinit var thumbnail: String

    override fun bind(holder: RedditPostHolder) {
        with(holder) {
            postTitle.text = title
            postDescription.text = description
            Glide.with(holder.postThumbnail).apply {
                when (thumbnail.isValidUrl()) {
                    true -> load(thumbnail).into(holder.postThumbnail)
                    false -> {
                        clear(holder.postThumbnail)
                        holder.postThumbnail.visibility = View.GONE
                    }
                }
            }
        }
    }

    inner class RedditPostHolder : EpoxyHolder() {

        lateinit var postTitle: TextView
        lateinit var postDescription: TextView
        lateinit var postThumbnail: ImageView

        override fun bindView(itemView: View) {
            postTitle = itemView.title
            postDescription = itemView.description
            postThumbnail = itemView.thumbnail
        }

    }
}