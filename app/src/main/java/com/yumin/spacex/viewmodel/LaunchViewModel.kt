package com.yumin.spacex.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumin.spacex.R
import com.yumin.spacex.common.Event
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
        groupItemList.add(0, GroupItem("Core",mutableListOf(item)))
        groupItemList.add(1, GroupItem("Payload",mutableListOf(item)))
        groupItemList.add(2, GroupItem("Links",mutableListOf(item)))
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