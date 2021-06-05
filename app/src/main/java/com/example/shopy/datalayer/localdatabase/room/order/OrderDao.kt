package com.example.shopy.datalayer.localdatabase.room.order

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.Product

@Dao
interface OrderDao {
    @Query("SELECT * FROM OrderObject")
    fun getAllOrderList(): LiveData<List<OrderObject>>
}