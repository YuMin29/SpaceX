package com.yumin.spacex.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.yumin.spacex.R
import com.yumin.spacex.databinding.GroupListLayoutBinding
import com.yumin.spacex.databinding.ItemCoreLayoutBinding
import com.yumin.spacex.databinding.ItemLinksLayoutBinding
import com.yumin.spacex.databinding.ItemPayloadLayoutBinding
import com.yumin.spacex.model.ExpandableItem
import com.yumin.spacex.model.RocketItem

open class ExpandableAdapter(
    private val context: Context,
    private var expandableData: ExpandableItem
) : BaseExpandableListAdapter() {

    companion object {
        const val CHILD_COUNT = 3
        const val TYPE_CHILD_CORE = 0
        const val TYPE_CHILD_PAYLOAD = 1
        const val TYPE_CHILD_LINKS = 2
    }

    fun setExpandableItem(item: ExpandableItem) {
        expandableData = item
    }

    override fun getChildTypeCount(): Int {
        return CHILD_COUNT
    }

    override fun getGroupCount(): Int {
        return expandableData.groupList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): String {
        return expandableData.groupList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): RocketItem {
        return expandableData.childItem
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
        var binding = GroupListLayoutBinding.inflate(LayoutInflater.from(context))
        binding.groupName.text = getGroup(groupPosition)

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
                    val itemCoreLayoutBinding =
                        ItemCoreLayoutBinding.inflate(LayoutInflater.from(context))
                    val coreInfo = getChild(groupPosition, childPosition).rocket.firstStage.cores[0]
                    itemCoreLayoutBinding.coreSerial.text = coreInfo.coreSerial

                    coreInfo.block.toString().let {
                        if (it.isNullOrEmpty())
                            itemCoreLayoutBinding.blockVal.text =
                                context.getString(R.string.no_info)
                        else
                            itemCoreLayoutBinding.blockVal.text = it
                    }

                    itemCoreLayoutBinding.flightVal.text = coreInfo.flight.toString()

                    coreInfo.landingType.let {
                        if (it.isNullOrEmpty())
                            itemCoreLayoutBinding.landTypeVal.text =
                                context.getString(R.string.no_info)
                        else
                            itemCoreLayoutBinding.landTypeVal.text = it
                    }

                    itemCoreLayoutBinding.reusedVal.text = coreInfo.reused.toString()
                    return itemCoreLayoutBinding.root
                }

                TYPE_CHILD_PAYLOAD -> {
                    val itemPayloadLayoutBinding =
                        ItemPayloadLayoutBinding.inflate(LayoutInflater.from(context))
                    val payloadInfo =
                        getChild(groupPosition, childPosition).rocket.secondStage.payloads[0]
                    itemPayloadLayoutBinding.payloadId.text = payloadInfo.payloadId

                    payloadInfo.capSerial.let {
                        if (it.isNullOrEmpty())
                            itemPayloadLayoutBinding.capVal.text =
                                context.getString(R.string.no_info)
                        else
                            itemPayloadLayoutBinding.capVal.text = it
                    }

                    itemPayloadLayoutBinding.payloadVal.text = payloadInfo.payloadType
                    itemPayloadLayoutBinding.manufacturerVal.text = payloadInfo.manufacturer
                    itemPayloadLayoutBinding.nationalityVal.text = payloadInfo.nationality
                    return itemPayloadLayoutBinding.root
                }

                TYPE_CHILD_LINKS -> {
                    val itemLinksLayoutBinding =
                        ItemLinksLayoutBinding.inflate(LayoutInflater.from(context))
                    val linksInfo = getChild(groupPosition, childPosition).links
                    linksInfo.youtubeId.let {
                        if (it.isNullOrEmpty())
                            itemLinksLayoutBinding.youtubeIdVal.text =
                                context.getText(R.string.no_info)
                        else
                            itemLinksLayoutBinding.youtubeIdVal.text = it
                    }
                    return itemLinksLayoutBinding.root
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