package com.yumin.spacex.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumin.spacex.R
import com.yumin.spacex.common.Event
import com.yumin.spacex.model.ChildItem
import com.yumin.spacex.model.GroupItem
import com.yumin.spacex.model.RocketItem
import com.yumin.spacex.repository.RemoteRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class LaunchViewModel constructor(private val repository: RemoteRepository) : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var sortOldest = MutableLiveData<Boolean>()
    var sortChecked = MutableLiveData<Int>()
    var rocketList = MutableLiveData<List<RocketItem>>()
    var selectedItem: MutableLiveData<RocketItem> = MutableLiveData()
    var openItemEvent: MutableLiveData<Event<RocketItem>> = MutableLiveData()
    var groupList = MutableLiveData<List<GroupItem>>()

    init {
        loadData()
        sortOldest.postValue(true)
        sortChecked.postValue(R.id.sortOldest)
    }

    private fun loadData() {
        repository.getRocket()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<RocketItem>> {
                override fun onSubscribe(d: Disposable) {
                    isLoading.postValue(true)
                }

                override fun onSuccess(list: List<RocketItem>) {
                    for (item in list) {
                        item.launchDateUtc.also { item.launchDateUtc = formatTime(it) }
                    }
                    rocketList.postValue(list)
                    isLoading.postValue(false)
                }

                override fun onError(e: Throwable) {
                    Log.e("[LaunchViewModel]", "[onError] ${e.message}")
                    isLoading.postValue(false)
                }
            })
    }

    fun sortChanged(useOldest: Boolean) {
        sortOldest.postValue(useOldest)

        if (useOldest)
            sortChecked.postValue(R.id.sortOldest)
        else
            sortChecked.postValue(R.id.sortNewest)

        val list = rocketList.value
        rocketList.postValue(list?.reversed())
    }

    fun openItem(item: RocketItem) {
        selectedItem.postValue(item)
        openItemEvent.postValue(Event(item))
        groupList.postValue(generateGroupData(item))
    }

    private fun generateGroupData(item: RocketItem): MutableList<GroupItem> {
        var groupItemList: MutableList<GroupItem> = mutableListOf()

        if (item.rocket.firstStage != null) {
            var coreSerial = ""
            val childList: MutableList<ChildItem> = mutableListOf()
            for (core in item.rocket.firstStage.cores) {
                core.coreSerial?.let { coreSerial = it }

                core.block?.let { childList.add(ChildItem("Block", it.toString())) }
                    ?: { childList.add(ChildItem("Block", "No info")) }

                core.flight?.let {
                    childList.add(ChildItem("Flight", it.toString()))
                } ?: {
                    childList.add(ChildItem("Flight", "No info"))
                }

                core.reused?.let {
                    childList.add(ChildItem("Reused", if (it) "Yes" else "No"))
                } ?: {
                    childList.add(ChildItem("Reused", "No info"))
                }

                core.landingType?.let {
                    childList.add(ChildItem("Landing", it))
                } ?: {
                    childList.add(ChildItem("Landing", "No info"))
                }
            }
            groupItemList.add(GroupItem(0, "Core", false, childList, coreSerial))
        }

        if (item.rocket.secondStage.payloads != null) {
            val childList: MutableList<ChildItem> = mutableListOf()
            for (payload in item.rocket.secondStage.payloads) {
                payload.capSerial?.let {
                    childList.add(ChildItem("capSerial", it))
                } ?: {
                    childList.add(ChildItem("capSerial", "No info"))
                }

                payload.cargoManifest?.let {
                    childList.add(ChildItem("cargoManifest", it))
                } ?: {
                    childList.add(ChildItem("cargoManifest", "No info"))
                }

                payload.payloadId?.let {
                    childList.add(ChildItem("payloadId", it))
                } ?: {
                    childList.add(ChildItem("payloadId", "No info"))
                }

                payload.manufacturer?.let {
                    childList.add(ChildItem("manufacturer", it))
                } ?: {
                    childList.add(ChildItem("manufacturer", "No info"))
                }
            }
            groupItemList.add(GroupItem(1, "Payload", false, childList, ""))
        }
        return groupItemList
    }

    private fun formatTime(originDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Taipei")
        val date: Date = inputFormat.parse(originDate)
        return outputFormat.format(date)
    }
}