package com.yumin.spacex.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingUtil {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String?) {
            val context = imageView.context
            Glide.with(context).load(url).thumbnail(0.25f).into(imageView)
        }
    }
}