package com.example.shopy.ui.productDetailsActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R

class OptionsAdapter(options : List<String>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    var options : List<String> = ArrayList()
    set(value) {
        field = value
    }
    init {
        this.options= options
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).
        inflate(R.layout.options_item, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorItemRecycler = itemView.findViewById<TextView>(R.id.colorItemRecycler)
        fun binding(options: String?) {
//            colorItemRecycler.text= options.values?.get(optionPosition) ?:""
            colorItemRecycler.text= options?:"not known"
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(options[position])
    }

    override fun getItemCount(): Int {
        return  options.size
    }
}