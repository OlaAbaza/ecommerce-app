package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.SubcategoryTitleBinding

class SubCategoriesAdapter(var subCategories:Array<String>,var subCategoryRecyclerClick: SubCategoryRecyclerClick):
    RecyclerView.Adapter<SubCategoriesAdapter.SubCategoriesViewHolder>() {
    var oldPos=0
    class SubCategoriesViewHolder(val binding: SubcategoryTitleBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoriesViewHolder {
        return SubCategoriesViewHolder(SubcategoryTitleBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: SubCategoriesViewHolder, position: Int) {
        holder.binding.subCategoryTitle.text=subCategories.get(position)
        if (holder.adapterPosition==oldPos){
            holder.binding.underLine.background= ColorDrawable(Color.parseColor("#000000"))
        }else{
            holder.binding.underLine.background= ColorDrawable(Color.parseColor("#ffffff"))
        }
        holder.itemView.setOnClickListener {
            subCategoryRecyclerClick.onSubClick(position)
            oldPos=position
        }
    }

    override fun getItemCount(): Int = subCategories.size
}