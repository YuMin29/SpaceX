package com.yumin.spacex.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yumin.spacex.R
import com.yumin.spacex.databinding.LaunchItemBinding
import com.yumin.spacex.model.RocketItem

class RecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var rocketList: List<RocketItem> = emptyList()
    var clickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding = DataBindingUtil.inflate<LaunchItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.launch_item,
            parent,
            false
        )
        return LaunchItemViewHolder(itemBinding, clickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return rocketList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    fun updateItems(items: List<RocketItem>?) {
        rocketList = items ?: emptyList()
        notifyDataSetChanged()
    }

    inner class LaunchItemViewHolder(
        private val binding: LaunchItemBinding,
        private val listener: OnItemClickListener?
    ) :
        BaseViewHolder(binding.root), View.OnClickListener {
        override fun onBind(position: Int) {
            binding.rocketItem = rocketList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            listener?.onItemClick(view, rocketList[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, item: RocketItem)
    }
}