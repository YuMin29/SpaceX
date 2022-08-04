package com.yumin.spacex.model


data class GroupItem(
    var id: Int = 0,
    var groupName: String = "",
    var isSelected: Boolean? = null,
    var childItemList: List<ChildItem> = emptyList(),
    var childTitle: String? = null
)