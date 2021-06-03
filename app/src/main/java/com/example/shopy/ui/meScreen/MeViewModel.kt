package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.WishRoomRepositry

class MeViewModel(application : Application): ViewModel() {
    private var wishListLiveData : MutableLiveData<List<Product>> = MutableLiveData()

    private val wishRoomRepo = WishRoomRepositry(application)

    fun getAllData() = wishRoomRepo.getAllWishList()

}