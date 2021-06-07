package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.shopy.databinding.SubcategoryTitleBinding

class MainCategoriesAdapter(
    var mainCategories: Array<String>,
    var mainCategoryRecyclerClick: MainCategoryRecyclerClick
) :
    RecyclerView.Adapter<MainCategoriesAdapter.MainCategoriesViewHolder>() {
    var oldPos = 0

    class MainCategoriesViewHolder(val binding: SubcategoryTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoriesViewHolder {
        return MainCategoriesAdapter.MainCategoriesViewHolder(
            SubcategoryTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MainCategoriesViewHolder, position: Int) {
        holder.binding.subCategoryTitle.text = mainCategories.get(position)
        if (holder.adapterPosition == oldPos) {
            holder.binding.underLine.background = ColorDrawable(Color.parseColor("#000000"))
        } else {
            holder.binding.underLine.background = ColorDrawable(Color.parseColor("#ffffff"))
        }

        holder.itemView.setOnClickListener {
            holder.binding.underLine.background = ColorDrawable(Color.parseColor("#000000"))
            mainCategoryRecyclerClick.onMainClick(position)
            oldPos = position
        }
    }

    override fun getItemCount(): Int {
        return mainCategories.size
    }
}