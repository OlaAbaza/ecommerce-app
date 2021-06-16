package com.example.shopy.ui.showOneOrderDetails

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce

class OrdersListAdapter(line_items: List<OneOrderResponce.Order.LineItem?>,images : List<String>?) : RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {
    var line_items: List<OneOrderResponce.Order.LineItem?> = line_items
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    var images:  List<String>? = images
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  title =itemView.findViewById<TextView>(R.id.title)
        val  priceEdible =itemView.findViewById<TextView>(R.id.priceEdible)
        val  variant_Title =itemView.findViewById<TextView>(R.id.variant_Title)
        val  quantityEditable =itemView.findViewById<TextView>(R.id.quantityEditable)
        val  itemImageOrder =itemView.findViewById<ImageView>(R.id.itemImageOrder)
        fun binding(lineItem: OneOrderResponce.Order.LineItem?,image :String?) {
            title.text = lineItem?.title ?: "Not Known"
            priceEdible.text = lineItem?.price ?: "Not Known"
            variant_Title.text = lineItem?.variant_title ?: "Not Known"
            quantityEditable.text = lineItem?.quantity.toString()
            Glide.with(itemImageOrder)
                .load(image?: "")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .into(itemImageOrder)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.one_order_items_details, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images!!.isEmpty()){
            holder.binding(line_items[position], "")
        }else{
            holder.binding(line_items[position], images?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return line_items.size
    }
}