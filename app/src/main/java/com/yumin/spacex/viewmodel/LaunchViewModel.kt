package com.yumin.spacex.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumin.spacex.R
import com.yumin.spacex.common.Event
import com.yumin.spacex.model.ExpandableItem
import com.yumin.spacex.model.RocketItem
import com.yumin.spacex.repository.RemoteRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class LaunchViewModel constructor(
    private val repository: RemoteRepository,
    private val context: Context
) : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var sortOldest = MutableLiveData<Boolean>()
    var rocketList = MutableLiveData<List<RocketItem>>()
    var selectedRocketItem: MutableLiveData<RocketItem> = MutableLiveData()
    var openItemEvent: MutableLiveData<Event<RocketItem>> = MutableLiveData()
    var expandableItem = MutableLiveData<ExpandableItem>()

    init {
        loadData()
        sortOldest.value = true // default use oldest
    }

    companion object {
        const val DATE_INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DATE_OUTPUT_PATTERN = "dd-MM-yyyy HH:mm:ss"
        const val TIME_ZONE = "Asia/Taipei"
    }

    private fun loadData() {
        repository.getRocket()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<RocketItem>> {
                override fun onSubscribe(d: Disposable) {
                    isLoading.value = true
                }

                override fun onSuccess(list: List<RocketItem>) {
                    Log.d("[LaunchViewModel]", "[onSuccess] list size = ${list.size}")
                    for (item in list) {
                        item.launchDateUtc.also { item.launchDateUtc = formatTime(it) }
                    }
                    rocketList.value = list
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    Log.e("[LaunchViewModel]", "[onError] ${e.message}")
                    isLoading.value = false
                }
            })
    }

    fun sortChanged(useOldest: Boolean) {
        sortOldest.value = useOldest
        rocketList.value = rocketList.value?.reversed()
    }

    fun openRocketItem(item: RocketItem) {
        selectedRocketItem.value = item
        openItemEvent.value = Event(item)
        expandableItem.value = generateExpandableItem(item)
    }

    private fun generateExpandableItem(item: RocketItem): ExpandableItem {
        val groupList = listOf(
            context.getString(R.string.core),
            context.getString(R.string.payloads),
            context.getString(R.string.links)
        )
        return ExpandableItem(groupList, item)
    }

    private fun formatTime(originDate: String): String {
        val inputFormat = SimpleDateFormat(DATE_INPUT_PATTERN)
        val outputFormat = SimpleDateFormat(DATE_OUTPUT_PATTERN)
        outputFormat.timeZone = TimeZone.getTimeZone(TIME_ZONE)
        val date: Date = inputFormat.parse(originDate)
        return outputFormat.format(date)
    }
}