package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopy.datalayer.entity.WishItem
import com.example.shopy.datalayer.localdatabase.room.WishRoomRepositry

class MeViewModel(application : Application): ViewModel() {
    private var wishListLiveData : MutableLiveData<List<WishItem>> = MutableLiveData()

    private val wishRoomRepo = WishRoomRepositry()

    fun getAllData() = wishRoomRepo.getAllWishList()

}