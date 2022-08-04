package com.yumin.spacex.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.yumin.spacex.R
import com.yumin.spacex.databinding.ChildItemLayoutBinding
import com.yumin.spacex.databinding.GroupListLayoutBinding
import com.yumin.spacex.model.ChildItem
import com.yumin.spacex.model.GroupItem

open class ExpandableAdapter : BaseExpandableListAdapter() {
    private val context: Context? = null
    private var groupListData: List<GroupItem> = emptyList()

    fun setGroupList(list: List<GroupItem>) {
        groupListData = list
    }

    override fun getGroupCount(): Int {
        return groupListData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return groupListData.get(groupPosition).childItemList.size
    }

    override fun getGroup(groupPosition: Int): GroupItem {
        return groupListData.get(groupPosition)
    }

    override fun getChild(groupPosition: Int, childPosition: Int): ChildItem {
        return groupListData.get(groupPosition).childItemList.get(childPosition)
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
            var binding = DataBindingUtil.inflate<ChildItemLayoutBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.child_item_layout,
                parent,
                false
            )
            binding.groupItem = getGroup(groupPosition)
            binding.childItem = getChild(groupPosition, childPosition)
            return binding.root
        }
        return convertView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return false
    }

}