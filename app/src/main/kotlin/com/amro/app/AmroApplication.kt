package com.amro.app

import android.app.Application
import com.amro.core.database.di.databaseModule
import com.amro.core.network.di.networkModule
import com.amro.feature.movies.di.moviesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AmroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AmroApplication)
            modules(
                networkModule(
                    apiKey = BuildConfig.TMDB_API_KEY,
                    isDebug = BuildConfig.DEBUG,
                ),
                databaseModule,
                moviesModule,
            )
        }
    }
}
