package com.aengussong.beddit.di

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Room
import com.aengussong.beddit.model.RedditPost
import com.aengussong.beddit.repo.RedditRepo
import com.aengussong.beddit.repo.local.RedditDatabase
import com.aengussong.beddit.repo.local.dao.PostDao
import com.aengussong.beddit.repo.remote.RedditService
import com.aengussong.beddit.ui.main.MainViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(OkHttpClient())
            .build()
    }

    single<RedditService> {
        get<Retrofit>().create(RedditService::class.java)
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), RedditDatabase::class.java, "reddit.db")
            .build()
    }

    single {
        get<RedditDatabase>().postDao()
    }
}

val pagingModule = module {
    factory<LivePagedListBuilder<Int, RedditPost>> {
        LivePagedListBuilder(
            get<PostDao>().getPosts(),
            PagedList.Config
                .Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(30)
                .setPageSize(30)
                .setPrefetchDistance(15)
                .build()
        )
    }
}

val repoModule = module{
    factory { RedditRepo(get(), get(), get()) }
}

val viewModelModule = module{
    viewModel { MainViewModel(get()) }
}