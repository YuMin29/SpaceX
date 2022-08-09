package com.yumin.spacex.model


data class ExpandableItem(
    val groupList: List<String>,
    val childItem: RocketItem
)