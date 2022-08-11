package com.yumin.spacex.repository

import com.yumin.spacex.model.RocketItem
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteRepository {
    private var remoteApiService: RemoteApiService = RemoteApiManager.remoteApiService

    suspend fun getRocket(): List<RocketItem> {
        return suspendCancellableCoroutine {
            remoteApiService.getRocket().enqueue(
                object : Callback<List<RocketItem>> {
                    override fun onResponse(
                        call: Call<List<RocketItem>>,
                        response: Response<List<RocketItem>>
                    ) {
                        it.resumeWith(Result.success(response.body()) as Result<List<RocketItem>>)
                    }

                    override fun onFailure(call: Call<List<RocketItem>>, t: Throwable) {
                        it.resumeWith(Result.failure(t))
                    }
                }
            )
        }
    }
}