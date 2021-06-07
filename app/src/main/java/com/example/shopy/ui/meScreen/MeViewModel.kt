package com.example.shopy.ui.meScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.wishBag.WishRoomRepositry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeViewModel(val repository: Repository, application: Application): AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()
    var deleteItem : MutableLiveData<Product> = MutableLiveData()


    fun getFourWishList() = repository.getFourWishList()

    fun deleteOneItemFromWishList(id : Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteOneWishItem(id)
        }
    }

}