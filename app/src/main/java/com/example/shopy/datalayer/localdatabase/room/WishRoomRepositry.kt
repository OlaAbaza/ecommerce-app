package com.example.shopy.datalayer.localdatabase.room

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.shopy.datalayer.entity.WishItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishRoomRepositry() {
    val database :RoomService?=RoomService.getInstance()
    val wishDao :WishDao= database!!.wishDao()

    fun getAllWishList() = wishDao.getAllWishList()

    fun saveWishList(withItem: WishItem) {
        CoroutineScope(Dispatchers.IO).launch {
            wishDao.saveWishList(withItem)
        }
    }


    fun deleteOneWishItem(withItem: WishItem) {
        CoroutineScope(Dispatchers.IO).launch {
            wishDao.deleteOneWithItem(withItem)
        }
    }


}