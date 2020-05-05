package com.aengussong.beddit.app

import android.app.Application
import com.aengussong.beddit.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BedditApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BedditApp)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    repoModule,
                    pagingModule,
                    viewModelModule
                )
            )
        }
    }
}