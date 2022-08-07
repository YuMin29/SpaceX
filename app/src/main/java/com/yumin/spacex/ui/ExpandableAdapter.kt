package com.yumin.spacex.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yumin.spacex.R
import com.yumin.spacex.databinding.GroupListLayoutBinding
import com.yumin.spacex.databinding.ItemCoreLayoutBinding
import com.yumin.spacex.databinding.ItemLinksLayoutBinding
import com.yumin.spacex.databinding.ItemPayloadLayoutBinding
import com.yumin.spacex.model.ChildItem
import com.yumin.spacex.model.GroupItem
import com.yumin.spacex.model.RocketItem

open class ExpandableAdapter : BaseExpandableListAdapter() {
    private val context: Context? = null
    private var groupListData: List<GroupItem> = emptyList()

    companion object {
        const val TYPE_CHILD_CORE = 0
        const val TYPE_CHILD_PAYLOAD = 1
        const val TYPE_CHILD_LINKS = 2
    }

    fun setGroupList(list: List<GroupItem>) {
        groupListData = list
    }

    override fun getChildTypeCount(): Int {
        return 3
    }

    override fun getGroupCount(): Int {
        return groupListData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return groupListData[groupPosition].childItemList.size
    }

    override fun getGroup(groupPosition: Int): GroupItem {
        return groupListData[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): RocketItem {
        return groupListData[groupPosition].childItemList[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var binding = DataBindingUtil.inflate<GroupListLayoutBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.group_list_layout,
            parent,
            false
        )
        binding.groupItem = getGroup(groupPosition)

        if (isExpanded)
            binding.image.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        else
            binding.image.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24)

        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        if (convertView == null) {
            when (getChildType(groupPosition, childPosition)) {
                TYPE_CHILD_CORE -> {
                    val binding = DataBindingUtil.inflate<ItemCoreLayoutBinding>(
                        LayoutInflater.from(parent?.context),
                        R.layout.item_core_layout,
                        parent,
                        false
                    )
                    binding.coreData = getChild(groupPosition, childPosition).rocket.firstStage.cores[0]
                    return  binding.root
                }

                TYPE_CHILD_PAYLOAD -> {
                    val binding = DataBindingUtil.inflate<ItemPayloadLayoutBinding>(
                        LayoutInflater.from(parent?.context),
                        R.layout.item_payload_layout,
                        parent,
                        false
                    )
                    binding.payloadData = getChild(groupPosition, childPosition).rocket.secondStage.payloads[0]
                    return  binding.root
                }

                TYPE_CHILD_LINKS -> {
                    val binding = DataBindingUtil.inflate<ItemLinksLayoutBinding>(
                        LayoutInflater.from(parent?.context),
                        R.layout.item_links_layout,
                        parent,
                        false
                    )
                    binding.linkData = getChild(groupPosition, childPosition).links
                    return  binding.root
                }
            }
        }
        return convertView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return false
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return when (groupPosition) {
            0 -> TYPE_CHILD_CORE
            1 -> TYPE_CHILD_PAYLOAD
            2 -> TYPE_CHILD_LINKS
            else -> TYPE_CHILD_CORE
        }
    }
}