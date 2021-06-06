package com.example.shopy.dataLayer.localdatabase.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.localdatabase.room.cartBag.CartDao

class RoomDataSourceImpl(val database: RoomService?) : RoomDataSource {

    val cartDao: CartDao = database!!.caerDao()
    override fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return cartDao.getAllCartList()
    }

    override suspend fun saveCartList(withItem: ProductCartModule) {
        cartDao.saveCartList(withItem)
    }

    override suspend fun deleteOnCartItem(id: Long) {
        cartDao.deleteOnCartItem(id)
    }
}