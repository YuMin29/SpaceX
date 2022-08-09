package com.yumin.spacex.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteApiManager private constructor() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(RemoteApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .build()
    }

    companion object {
        private var retrofitManager: RemoteApiManager? = null
        @Synchronized
        fun newInstance(): RemoteApiManager {
            if (retrofitManager == null) {
                retrofitManager = RemoteApiManager()
            }
            return retrofitManager as RemoteApiManager
        }
    }

    fun <T> create(t: Class<T>): T {
        return retrofit.create(t)
    }
}