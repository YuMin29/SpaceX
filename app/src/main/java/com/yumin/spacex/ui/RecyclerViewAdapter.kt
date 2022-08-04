package com.yumin.spacex.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yumin.spacex.R
import com.yumin.spacex.databinding.LaunchItemBinding
import com.yumin.spacex.model.RocketItem

class RecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var rocketList: List<RocketItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding = DataBindingUtil.inflate<LaunchItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.launch_item,
            parent,
            false
        )
        return LaunchItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return rocketList.size
    }


    fun updateItems(items: List<RocketItem>?) {
        rocketList = items ?: emptyList()
        notifyDataSetChanged()
    }

    inner class LaunchItemViewHolder(private val binding: LaunchItemBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.rocketItem = rocketList[position]
            binding.executePendingBindings()
        }
    }

}