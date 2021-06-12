package com.example.shopy.ui.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.models.CustomerOrder
import com.example.shopy.models.LineItem
import com.example.shopy.models.Order
import com.example.shopy.models.Orders
import java.util.*

class PaymentViewModel(val repository: Repository, application: Application) : AndroidViewModel(
    application
) {

    fun createOrderInPayment(order: GetOrders.Order) {
        val customerOrder = CustomerOrder(order.customer!!.id)
        var lineItems: MutableList<LineItem> = arrayListOf()
        for (i in order.line_items!!.indices) {
            lineItems.add(
                LineItem(
                    order.line_items[i]!!.quantity,
                    order.line_items[i]!!.variant_id
                )
            )
        }
        val ord = Order(customerOrder, "paid", lineItems, "card")
        val orders = Orders(ord)
        repository.createOrder(orders)
    }

    fun cancelOrder(orderId : Long){
        repository.deleteOrder(orderId)
    }


}