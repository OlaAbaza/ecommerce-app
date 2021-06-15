package com.example.shopy.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.datalayer.entity.itemPojo.Images
import java.util.*
import kotlin.collections.ArrayList


//class ImageSilderAdapter {
//}

class ImageSilderAdapter(images: List<Images>) : PagerAdapter(){

    // var context: Context
     var images: List<Images> = ArrayList()
         set(value) {
         field = value
     }
    init {
     //   this.context=context
        this.images=images
    }
    override fun getCount(): Int {
       return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(
            R.layout.image_slider_item,
            container,
            false
        )

        val imageView = itemView.findViewById<ImageView>(R.id.imageViewMain)
        val text = itemView.findViewById<TextView>(R.id.itemNums)
        val t = "${position + 1} / ${images.size}"
        text.text = t
        Glide.with(imageView)
            .load(images[position].src ?: "")
            .fitCenter()
            .placeholder(R.drawable.ic_loading)
            .into(imageView)

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }


}