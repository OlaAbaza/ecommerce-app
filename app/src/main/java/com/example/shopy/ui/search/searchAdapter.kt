package com.example.shopy.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.datalayer.entity.allproducts.allProduct


class searchAdapter(private val context: Context, private val itemName : List<allProduct>):
        RecyclerView.Adapter<searchAdapter.ViewHolderExample>(){

    lateinit var filterList : List<allProduct>

    init {
        filterList = itemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderExample {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shop_item,parent,false)
        return ViewHolderExample(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolderExample, position: Int) {
        holder.itemName.text = filterList.get(position).vendor

        Glide.with(context)
                .load( filterList.get(position).image.src )
                .into(holder.itemIcon)
        //String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
//https://cdn.shopify.com/s/files/1/0567/9310/4582/products/7883dc186e15bf29dad696e1e989e914.jpg?v=1621288214


    }
    override fun getItemCount() = filterList.size

    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName: TextView = itemView.findViewById(R.id.itemTitle)
        val itemIcon : ImageView = itemView.findViewById(R.id.itemIcon)
    }

}