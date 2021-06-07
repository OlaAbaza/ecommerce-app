package com.example.shopy.adapters

import android.util.Log
import com.example.shopy.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.datalayer.entity.itemPojo.Product


class WishListAdaper(
    prouductList: List<Product>,
    intentTOProductDetails: MutableLiveData<Product>,
    deleteItem: MutableLiveData<Product>
) : RecyclerView.Adapter<WishListAdaper.ViewHolder>() {
    val intentTOProductDetails: MutableLiveData<Product> = intentTOProductDetails
    val deleteItem: MutableLiveData<Product> = deleteItem

    var productList: List<Product> = ArrayList()
        set(value) {
            field = value
        }

    init {
        this.productList = prouductList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.wish_list_item, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(productList[position], position)
        holder.itemView.setOnClickListener {
            intentTOProductDetails.value = productList[position]
            Log.d("TAG", "onBindViewHolder fired")

        }
        holder.deleteButton.setOnClickListener {
            deleteItem.value = productList[position]
        }
    }

    override fun getItemCount(): Int {
        return productList.count()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemViewa: View = itemView
        private val companyLogo = itemView.findViewById<ImageView>(R.id.itemImage)
        private val itemTitle = itemView.findViewById<TextView>(R.id.title)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.delete)
        fun binding(item: Product, position: Int) {
            Glide.with(companyLogo)
                .load(item.image.src ?: "")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .into(companyLogo)
            itemTitle.text = item.title ?: "Title name is unknown"


        }
    }

}