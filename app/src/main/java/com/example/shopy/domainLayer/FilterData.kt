package com.example.shopy.domainLayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.entity.orderGet.GetOrders

class FilterData {
    companion object {
        fun getAllData(orders: List<GetOrders.Order?>): List<GetOrders.Order?> {

            val filteredList: MutableList<GetOrders.Order?> =ArrayList()

            orders.forEach(){item ->
                if (item?.customer!!.id == 5248282788038){
                    filteredList.add(item)
                    Log.d("TAG","${item.toString()}")
                    Log.d("TAG","${filteredList.size}")


                }
            }
            Log.d("Tag","bhjm")
            Log.d("TAG","dtatahhhhhhh${filteredList.size}")
            return filteredList
        }
    }
}