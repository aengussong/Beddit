package com.aengussong.beddit.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aengussong.beddit.R
import com.aengussong.beddit.adapter.RedditPostAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.bestLiveData.observe(this, Observer { pagedList ->
            val adapter = RedditPostAdapter()
            adapter.submitList(pagedList)
            reddit_recycler.adapter = adapter
        })

        viewModel.requestErrors.observe(this, Observer {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }
}
