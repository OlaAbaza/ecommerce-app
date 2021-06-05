package com.example.shopy.ui.shopTab

import android.R.attr.data
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.ui.shopTab.search.searchAdapter


class ShopItemsAdapter(private val context: Context, private val itemName: List<Product>):
    RecyclerView.Adapter<ShopItemsAdapter.ViewHolderExample>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shop_item,parent,false)
        return ViewHolderExample(itemView)
    }


    override fun onBindViewHolder(holder: ShopItemsAdapter.ViewHolderExample, position: Int) {
        holder.itemName.text = itemName.get(position).vendor

        Glide.with(context)
            .load( itemName.get(position).image.src )
            .into(holder.itemIcon)
    }

    override fun getItemCount() = itemName.size



    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName: TextView = itemView.findViewById(R.id.itemTitle)
        val itemIcon : ImageView = itemView.findViewById(R.id.itemIcon)
    }



}