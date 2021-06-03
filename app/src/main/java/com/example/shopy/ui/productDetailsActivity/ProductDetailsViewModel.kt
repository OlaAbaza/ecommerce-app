package com.example.shopy.ui.productDetailsActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.WishRoomRepositry
import com.example.shopy.datalayer.onlineDataLayer.NetWorking
import com.example.shopy.datalayer.onlineDataLayer.productDetailsService.ProuductDetailsReposatory

class ProductDetailsViewModel(application : Application): AndroidViewModel(application) {
//    private var wishListLiveData : MutableLiveData<List<Product>> = MutableLiveData()
    val networkingReposatory = ProuductDetailsReposatory(NetWorking.productDetailsDao)

    fun getProductByIdFromNetwork(id : Long){
        networkingReposatory.getProuduct(id)
    }
    fun observeProductDetails() = networkingReposatory.prouductDetaild
}