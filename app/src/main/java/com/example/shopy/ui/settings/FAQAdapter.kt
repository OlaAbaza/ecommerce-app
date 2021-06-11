package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.FaqItemBinding

import com.example.shopy.datalayer.entity.settings.FAQModel

class FAQAdapter(val faqList:List<FAQModel>):RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {
    class FAQViewHolder(val binding:FaqItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        return FAQViewHolder(FaqItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.binding.questionTV.text=faqList.get(position).question
        holder.binding.answerTV.text=faqList.get(position).answer
        holder.binding.questionTV.setOnClickListener {
            if (holder.binding.answerTV.visibility== View.GONE) {
                holder.binding.answerTV.visibility = View.VISIBLE
            }else{
                holder.binding.answerTV.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}