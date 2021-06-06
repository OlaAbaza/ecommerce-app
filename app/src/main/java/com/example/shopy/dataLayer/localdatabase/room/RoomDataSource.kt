package com.example.shopy.dataLayer.localdatabase.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule

interface RoomDataSource {

    fun getAllCartList(): LiveData<List<ProductCartModule>>
    suspend fun saveCartList(withItem: ProductCartModule)
    suspend fun deleteOnCartItem(id: Long)

}