package com.example.shopy.ui.displayOrderFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R

class ProductsImageAdapter( imageList: List<String>) : RecyclerView.Adapter<ProductsImageAdapter.ViewHolder>() {
    var imageList = imageList
    set(value) {
        field= value
        Log.d("TAG","size ProductsImageAdapter ${imageList.size}")
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImageOrder = itemView.findViewById<ImageView>(R.id.itemImageOrder)
        fun binding(image : String){
            Glide.with(itemImageOrder)
                .load(image?: "")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .into(itemImageOrder)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.order_image_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG","size ${imageList.size}")
        holder.binding(imageList[position])
    }

    override fun getItemCount(): Int {
      return  imageList.size
    }
}