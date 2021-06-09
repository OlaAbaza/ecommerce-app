package com.example.shopy.domainLayer

import android.annotation.SuppressLint
import com.example.shopy.dataLayer.entity.orderGet.GetOrders

class FilterData {
    companion object {

        @SuppressLint("LogNotTimber")
        fun getAllData(orders: List<GetOrders.Order?>, userId: Long): List<GetOrders.Order?> {
            return orders.filter { it!!.customer?.id == userId }
        }

        fun getUnPaidData(orders: List<GetOrders.Order?>): List<GetOrders.Order?> {
            return orders.filter { it!!.financial_status == "pending" }
        }

        fun getPaidData(orders: List<GetOrders.Order?>): List<GetOrders.Order?> {
            return orders.filter { it!!.financial_status == "paid" }
        }
    }
}
