package com.example.shopy.dataLayer.room

import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule

interface RoomDataSource {

    fun getAllCartList(): LiveData<List<ProductCartModule>>
    suspend fun saveCartList(withItem: ProductCartModule)
    suspend fun deleteOnCartItem(id: Long)

}