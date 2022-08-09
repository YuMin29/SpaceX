package com.yumin.spacex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yumin.spacex.repository.RemoteRepository

class ViewModelFactory(private val repository: RemoteRepository, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LaunchViewModel(repository, context) as T
    }
}