package com.aengussong.beddit.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aengussong.beddit.R
import com.aengussong.beddit.adapter.RedditPostAdapter
import com.aengussong.beddit.model.State
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeContainer.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshState.observe(this, Observer { state ->
            swipeContainer.isRefreshing = state == State.LOADING
        })

        viewModel.bestLiveData.observe(this, Observer { pagedList ->
            val adapter = RedditPostAdapter()
            adapter.submitList(pagedList)
            reddit_recycler.adapter = adapter
        })

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
