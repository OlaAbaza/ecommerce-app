package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.shopy.databinding.CategoryItemBinding
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.ui.category.ItemsRecyclerClick


class CategoryItemAdapter(var categoryItems:List<Product>, var context: Context,var onClick:ItemsRecyclerClick):
    RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>()  {
    class CategoryItemViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        Glide.with(context).load(categoryItems.get(position).images.get(0).src).into(holder.binding.itemIcon)
        holder.binding.itemTitle.text=""+categoryItems.get(position).title
        holder.itemView.setOnClickListener {
            onClick.itemOnClick(categoryItems.get(position).id.toLong())
        }
    }

    override fun getItemCount(): Int = categoryItems.size
}