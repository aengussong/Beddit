package com.aengussong.beddit.ui.epoxy

import com.aengussong.beddit.model.RedditPost
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController

class RedditPostController : PagedListEpoxyController<RedditPost>() {

    override fun buildItemModel(currentPosition: Int, item: RedditPost?): EpoxyModel<*> {
        return RedditPostModel_()
            .id(item?.name)
            .title(item?.title ?: "")
            .description(item?.selftext ?: "")
            .thumbnail(item?.thumbnail ?: "")
    }

}