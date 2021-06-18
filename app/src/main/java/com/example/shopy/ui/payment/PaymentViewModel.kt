package com.example.shopy.ui.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.models.CustomerOrder
import com.example.shopy.models.LineItem
import com.example.shopy.models.Order
import com.example.shopy.models.Orders

class PaymentViewModel(val repositoryImpl: IRepository, application: Application) : AndroidViewModel(
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
        val ord = Order(customerOrder, "paid", lineItems, "card", order.discount_codes)
        val orders = Orders(ord)
        repositoryImpl.createOrder(orders)
    }

    fun cancelOrder(orderId: Long){
        repositoryImpl.deleteOrder(orderId)
    }


}