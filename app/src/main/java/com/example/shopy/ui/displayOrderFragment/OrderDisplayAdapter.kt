package com.example.shopy.ui.displayOrderFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders

class OrderDisplayAdapter(list:
                          List<GetOrders.Order?>,
                          private var payNowMutableData: MutableLiveData<GetOrders.Order>,
                          private var cancelMutableData: MutableLiveData<GetOrders.Order>
) : RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder>() {
    var list:
            List<GetOrders.Order?> = list
    set(value) {
        field=value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order_display,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(list[position])
        holder.payNow.setOnClickListener {
            payNowMutableData.value=list[position]
        }
        holder.cancel.setOnClickListener {
            cancelMutableData.value=list[position]
        }
    }

    override fun getItemCount(): Int {
       return list.count()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId = itemView.findViewById<TextView>(R.id.orderIdEditable)
        private val textPrice = itemView.findViewById<TextView>(R.id.totalPriceEditable)
        private val currencyCode = itemView.findViewById<TextView>(R.id.currency_code)
        private val cash = itemView.findViewById<TextView>(R.id.cashText)
        private val createdAt = itemView.findViewById<TextView>(R.id.createdAtEditable)
         val payNow: Button = itemView.findViewById(R.id.payButton)
         val cancel: Button = itemView.findViewById(R.id.cancelButton)
        fun binding(order: GetOrders.Order?) {
            textId.text = order!!.id.toString()
            textPrice.text = order.total_price?:"not known"
            currencyCode.text = order.presentment_currency?:"not known"
            createdAt.text = order.created_at?:"not known"

            if (order.financial_status == "paid"){
                payNow.visibility=View.GONE
                cancel.visibility=View.GONE
            }else{
                payNow.visibility=View.VISIBLE
                cancel.visibility=View.VISIBLE
            }


            if (order.note=="Cash"){
                payNow.visibility=View.GONE
                cash.visibility=View.VISIBLE
            }else{
                payNow.visibility=View.VISIBLE
                cash.visibility=View.GONE
            }
        }

    }}