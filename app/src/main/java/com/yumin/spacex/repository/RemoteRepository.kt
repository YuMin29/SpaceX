package com.yumin.spacex.repository

import com.yumin.spacex.model.RocketItem
import io.reactivex.Single

class RemoteRepository {
    private var remoteApiService: RemoteApiService =
        RemoteApiManager.newInstance().create(RemoteApiService::class.java)

    fun getRocket(): Single<List<RocketItem>> {
        return remoteApiService.getRocket()
    }
}