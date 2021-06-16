package com.example.shopy.domainLayer

import android.annotation.SuppressLint
import android.util.Log
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.datalayer.entity.allproducts.allProduct

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


//        fun getProductsIDs(fulfillments: List<GetOrders.Order.LineItem?>): List<Long> {
//            return fulfillments.map { it!!.id }
//        }
        fun ProductsIDs(orders: List<GetOrders.Order?>): List<List<Long>> {
            var list : MutableList<List<Long>> = arrayListOf()
             orders.forEach {
                 order ->
                val  line_items= order?.line_items?.map { it!!.product_id }
                 list.add(line_items as List<Long>)
             }
            return list
        }

        fun getListOfImage(prouductsId: List<List<Long>>, products: List<allProduct>) : MutableList<List<String>> {
            val imagesOrders : MutableList<List<String>> = arrayListOf()

            for (list in prouductsId){
                val l: MutableList<String> = arrayListOf()

                for (id in list){

                    for (item in products){
                        if (id == item.id.toLong())
                            l.add(item.image.src)
                    }

                }
                if (list.isNotEmpty())
                    imagesOrders.add(l)

            }
            return imagesOrders
        }
    }
}
