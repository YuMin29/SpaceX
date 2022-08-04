package com.yumin.spacex.repository

import com.yumin.spacex.model.RocketItem
import io.reactivex.Single
import retrofit2.http.GET

interface RemoteApiService {

    @GET("launches")
    fun getRocket(): Single<List<RocketItem>>

    companion object {
        const val BASE_URL = "https://api.spacexdata.com/v3/"
    }
}