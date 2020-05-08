package com.aengussong.beddit.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aengussong.beddit.R
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.model.State
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private val controller: PagedListEpoxyController<RedditPost> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecycler()
        setUpRefresh()
        setUpRetry()
    }

    private fun setUpRecycler() {
        reddit_recycler.adapter = controller.adapter
        viewModel.postsLiveData.observe(this, Observer { pagedList ->
            controller.submitList(pagedList)
        })
    }

    private fun setUpRefresh() {
        swipeContainer.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshState.observe(this, Observer { state ->
            swipeContainer.isRefreshing = state == State.LOADING
        })
    }

    private fun setUpRetry() {
        viewModel.requestError.observe(this, Observer { retry ->
            Snackbar.make(coordinator_layout, "Error occurred", Snackbar.LENGTH_INDEFINITE).apply {
                setAction("Retry") {
                    retry.invoke()
                    dismiss()
                }
                show()
            }
        })
    }
}
