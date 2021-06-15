package com.example.shopy.ui.productDetailsActivity

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.datalayer.entity.itemPojo.Options

class OptionsAdapter(options: List<String>, optionsMutableLiveData: MutableLiveData<Int>) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    var positionLast: Int = -1
    var options: List<String> = ArrayList()
        set(value) {
            field = value
        }

    init {
        this.options = options
    }

    var optionsMutableLiveData: MutableLiveData<Int> = optionsMutableLiveData
        set(value) {
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.options_item, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorItemRecycler = itemView.findViewById<TextView>(R.id.colorItemRecycler)
        fun binding(options: String?) {
            colorItemRecycler.text = options ?: "not known"
        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(options[position])

        if (position == positionLast) {
            holder.colorItemRecycler.setBackgroundResource(R.color.green)
        }else{
            holder.colorItemRecycler.setBackgroundResource(R.color.gray)
        }

        holder.itemView.setOnClickListener {
            positionLast = position
            Log.d("TAG"," holder.itemView.setOnClickListener $position")
            optionsMutableLiveData.value = position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return options.size
    }
}