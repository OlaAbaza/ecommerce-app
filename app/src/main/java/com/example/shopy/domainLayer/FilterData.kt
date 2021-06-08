package com.example.shopy.domainLayer

import android.annotation.SuppressLint
import com.example.shopy.dataLayer.entity.orderGet.GetOrders

class FilterData {
    companion object {

        @SuppressLint("LogNotTimber")
        fun getAllData(orders: List<GetOrders.Order?>): List<GetOrders.Order?> {
            return orders.filter { it!!.customer?.id == 5248282788038 }
        }

        fun getUnPaidData(orders: List<GetOrders.Order?>): List<GetOrders.Order?> {
            return orders.filter { it!!.customer?.id == 5248282788038 }
        }
    }
}



//            orders.forEach(){item ->
//                if (item?.customer!!.id == 5248282788038){
//                    filteredList.add(item)
//                    Log.d("TAG","${item.toString()}")
//                    Log.d("TAG","${filteredList.size}")
//                }
//                Log.d("Tag","bhjm")