package com.example.shopy.datalayer.localdatabase.room.cartBag

import android.app.Application
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.RoomService

class CartRoomRepository(application: Application) {
    val database : RoomService?= RoomService.getInstance(application)
    val cartDao : CartDao = database!!.caerDao()

    fun getAllCartList() = cartDao.getAllCartList()

    suspend fun saveCartList(cartItem: ProductCartModule) = cartDao.saveCartList(cartItem)
    suspend fun deleteOneCartItem(id: Long) = cartDao.deleteOnCartItem(id)
}