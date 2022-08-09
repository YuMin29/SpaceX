package com.yumin.spacex.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yumin.spacex.R
import com.yumin.spacex.databinding.LaunchItemBinding
import com.yumin.spacex.model.RocketItem

class RecyclerViewAdapter(
    private val clickListener: OnItemClickListener,
    private val context: Context,
    private val rocketList: List<RocketItem>
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            LaunchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaunchItemViewHolder(itemBinding, clickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return rocketList.size
    }

    inner class LaunchItemViewHolder(
        private val binding: LaunchItemBinding,
        private val listener: OnItemClickListener
    ) :
        BaseViewHolder(binding.root), View.OnClickListener {
        override fun onBind(position: Int) {
            binding.flightNumber.text =
                context.getString(R.string.flight, rocketList[position].flightNumber)
            binding.missionName.text = rocketList[position].missionName
            binding.launchDate.text = rocketList[position].launchDateUtc
            Glide.with(context).load(rocketList[position].links.missionPatchSmall)
                .thumbnail(0.25f).into(binding.coverImageView)
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            listener.onItemClick(view, rocketList[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, item: RocketItem)
    }
}