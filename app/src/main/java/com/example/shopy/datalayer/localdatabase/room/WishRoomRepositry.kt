package com.example.shopy.datalayer.localdatabase.room

import android.app.Application
import com.example.shopy.datalayer.entity.itemPojo.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishRoomRepositry(application: Application) {
    val database :RoomService?=RoomService.getInstance(application)
    val wishDao :WishDao= database!!.wishDao()

    fun getAllWishList() = wishDao.getAllWishList()

    fun saveWishList(wishItem: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            wishDao.saveWishList(wishItem)
        }
    }


    fun deleteOneWishItem(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            wishDao.deleteOneWithItem(id)
        }
    }


}