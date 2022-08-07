package com.yumin.spacex.model


data class GroupItem(
    var groupName: String = "",
    var childItemList: List<RocketItem> = emptyList()
)