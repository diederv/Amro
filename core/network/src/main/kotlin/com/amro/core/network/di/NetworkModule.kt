package com.amro.core.network.di

import com.amro.core.network.TmdbApiService
import com.amro.core.network.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

private val json = Json { ignoreUnknownKeys = true }

fun networkModule(apiKey: String, isDebug: Boolean) = module {
    single { AuthInterceptor(apiKey) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (isDebug) HttpLoggingInterceptor.Level.BASIC
                    else HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
    }

    single<TmdbApiService> {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TmdbApiService::class.java)
    }
}
