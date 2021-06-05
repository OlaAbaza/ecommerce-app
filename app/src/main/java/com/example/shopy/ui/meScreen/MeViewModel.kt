package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.wishBag.WishRoomRepositry

class MeViewModel(application : Application): AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()

    private val wishRoomRepo = WishRoomRepositry(application)

    fun getFourWishList() = wishRoomRepo.getFourWishList()

}