package com.yumin.spacex.repository

import com.yumin.spacex.model.RocketItem
import retrofit2.Call
import retrofit2.http.GET

interface RemoteApiService {

    @GET("launches")
    fun getRocket(): Call<List<RocketItem>>
}