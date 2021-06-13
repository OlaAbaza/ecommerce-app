package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.SubcategoryTitleBinding

class SubCategoriesAdapter(var subCategories:Array<String>,var subCategoryRecyclerClick: SubCategoryRecyclerClick):
    RecyclerView.Adapter<SubCategoriesAdapter.SubCategoriesViewHolder>() {
    var oldPos:Int?=null
    class SubCategoriesViewHolder(val binding:SubcategoryTitleBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoriesViewHolder {
        return SubCategoriesViewHolder(SubcategoryTitleBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: SubCategoriesViewHolder, position: Int) {
        holder.binding.subCategoryTitle.text=subCategories.get(position)
        if (holder.adapterPosition==oldPos?:false){
            //black
            holder.binding.underLine.background= ColorDrawable(Color.parseColor("#000000"))
        }else{
            //white
            holder.binding.underLine.background= ColorDrawable(Color.parseColor("#ffffff"))
        }
        holder.itemView.setOnClickListener {
            oldPos=position
            if (holder.adapterPosition==oldPos){
                //black
                holder.binding.underLine.background= ColorDrawable(Color.parseColor("#000000"))
            }
            subCategoryRecyclerClick.onSubClick(position)

        }
    }

    override fun getItemCount(): Int = subCategories.size
}