package com.example.shopy.ui.allWishListFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.wishBag.WishRoomRepositry

class AllWishListViewModel(application: Application) : AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()

    private val wishRoomRepo = WishRoomRepositry(application)

    fun getAllWishList() = wishRoomRepo.getAllWishList()
}