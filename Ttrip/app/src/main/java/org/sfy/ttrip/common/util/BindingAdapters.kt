package org.sfy.ttrip.common.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.sfy.ttrip.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:normalImgUri")
    fun ImageView.setNormalImg(imgUri: String?) {
        Glide.with(this.context)
            .load(imgUri)
            .placeholder(R.drawable.bg_image_not_found)
            .error(R.drawable.bg_image_not_found)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("android:circleImgUri")
    fun ImageView.setProfileImg(imgUri: String?) {
        Glide.with(this.context)
            .load(imgUri)
            .placeholder(R.drawable.ic_profile_default)
            .error(R.drawable.ic_profile_default)
            .circleCrop()
            .into(this)
    }
}