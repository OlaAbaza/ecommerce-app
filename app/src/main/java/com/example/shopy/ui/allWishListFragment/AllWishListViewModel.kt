package com.example.shopy.ui.allWishListFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.datalayer.entity.itemPojo.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AllWishListViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()
    var deleteItem : MutableLiveData<Product> = MutableLiveData()
    lateinit var deleteOneItemFromWish :Job

    fun getAllWishList() = repository.getAllWishList()
    fun deleteOneItemFromWishList(id : Long) {
        deleteOneItemFromWish = CoroutineScope(Dispatchers.IO).launch {
            repository.deleteOneWishItem(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!deleteOneItemFromWish.isCancelled){
            deleteOneItemFromWish.cancel()
        }
    }
}