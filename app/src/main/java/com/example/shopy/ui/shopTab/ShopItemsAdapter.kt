package com.example.shopy.ui.shopTab

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.datalayer.entity.custom_product.Product


class ShopItemsAdapter(private val context: Context, private val itemName: List<Product>,var intentTOProductDetails : MutableLiveData<Product>):
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
        holder.itemView.setOnClickListener {
            intentTOProductDetails.value = itemName[position]
            Log.d("TAG","preesed ${itemName[position].id}")
        }
    }

    override fun getItemCount() = itemName.size



    class ViewHolderExample(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName: TextView = itemView.findViewById(R.id.itemTitle)
        val itemIcon : ImageView = itemView.findViewById(R.id.itemIcon)
    }



}