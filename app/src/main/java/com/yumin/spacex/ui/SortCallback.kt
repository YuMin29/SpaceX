package com.yumin.spacex.ui

import android.widget.RadioGroup

interface SortCallback {
    fun openSortDialog()
    fun sortChanged(radioGroup: RadioGroup, id: Int)
}