package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.WishRoomRepositry

class MeViewModel(application : Application): AndroidViewModel(application) {
    private var wishListLiveData : MutableLiveData<List<Product>> = MutableLiveData()

    private val wishRoomRepo = WishRoomRepositry(application)

    fun getAllData() = wishRoomRepo.getAllWishList()

}