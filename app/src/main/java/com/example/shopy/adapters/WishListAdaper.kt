package com.example.shopy.adapters

import com.example.shopy.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.datalayer.entity.itemPojo.Product

class WishListAdaper(prouductList: List<Product>) : RecyclerView.Adapter<WishListAdaper.ViewHolder>() {
    var jobList: List<Product> = ArrayList()
        set(value) {
            field = value
        }
    init {
        this.jobList = prouductList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdaper.ViewHolder {
        val binding = LayoutInflater.from(parent.context).
        inflate(R.layout.wish_list_item, parent, false)

        return ViewHolder(binding)    }

    override fun onBindViewHolder(holder: WishListAdaper.ViewHolder, position: Int) {
        holder.binding(jobList[position],position)
    }

    override fun getItemCount(): Int {
        return jobList.count()
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyLogo = itemView.findViewById<ImageView>(R.id.itemImage)
        private val jobTitle = itemView.findViewById<TextView>(R.id.title)
        private val decription = itemView.findViewById<TextView>(R.id.decrption)
        fun binding(jobsItem: Product, position: Int){
            Glide.with(companyLogo)
                .load(jobsItem.image?:"")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .into(companyLogo)
            jobTitle.text = jobsItem.title?:"Title name is unknown"
            decription.text = jobsItem.product_type?:"Description is unknown"
        }
    }

}