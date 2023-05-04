package org.sfy.ttrip.common.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.sfy.ttrip.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:normalImgUri")
    fun ImageView.setNormalImg(imgUri: String?) {
        Glide.with(this.context)
            .load("http://k8d104.p.ssafy.io:8081/images$imgUri")
            .placeholder(R.drawable.bg_image_not_found)
            .error(R.drawable.bg_image_not_found)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("android:circleImgUri")
    fun ImageView.setProfileImg(imgUri: String?) {
        Glide.with(this.context)
            .load("http://k8d104.p.ssafy.io:8081/images$imgUri")
            .placeholder(R.drawable.ic_profile_default)
            .error(R.drawable.ic_profile_default)
            .circleCrop()
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("android:profileImgUri")
    fun ImageView.setProfileImg(imgUri: Uri?) {
        Glide.with(this.context)
            .load(imgUri)
            .placeholder(R.drawable.ic_profile_default)
            .error(R.drawable.ic_profile_default)
            .circleCrop()
            .into(this)
    }
}