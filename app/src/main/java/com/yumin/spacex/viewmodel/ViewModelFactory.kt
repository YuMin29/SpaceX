package com.yumin.spacex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yumin.spacex.repository.RemoteRepository

class ViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LaunchViewModel(repository) as T
    }
}