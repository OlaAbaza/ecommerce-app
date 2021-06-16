package com.example.shopy.ui.showOneOrderDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders

class OrdersListAdapter(line_items: List<GetOrders.Order.LineItem?>) : RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {
    var line_items: List<GetOrders.Order.LineItem?> = line_items
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(lineItem: GetOrders.Order.LineItem?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.one_order_items_details, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(line_items[position])
    }

    override fun getItemCount(): Int {
        return line_items.size
    }
}