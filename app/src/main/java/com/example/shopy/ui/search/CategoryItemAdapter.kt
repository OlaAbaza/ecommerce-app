package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.shopy.databinding.CategoryItemBinding
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.ui.category.ItemsRecyclerClick


class SearchCategoryItemAdapter(var categoryItems:List<allProduct>, var context: Context,var onClick:ItemsRecyclerClick):
    RecyclerView.Adapter<SearchCategoryItemAdapter.CategoryItemViewHolder>()  {
    class CategoryItemViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        Glide.with(context).load(categoryItems.get(position).images!!.get(0).src).into(holder.binding.itemIcon)
        //holder.binding.itemTitle.text=categoryItems.get(position).title
        holder.binding.itemTitle.text=""+categoryItems.get(position).title
        //Log.d("hitler","position: "+position+" price: "+categoryItems.get(position).variants!!.get(0).price)
        holder.itemView.setOnClickListener {
            onClick.itemOnClick(categoryItems.get(position).id.toLong())
        }
    }

    override fun getItemCount(): Int = categoryItems.size
}