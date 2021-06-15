package com.example.shopy.ui.productDetailsActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.datalayer.entity.itemPojo.Options
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {

    private var saveWishListJob: Job? = null
    private var deleteOneWishItemJob: Job? = null
    private  var saveToCartJob : Job? = null
    var optionsMutableLiveData : MutableLiveData<Int> = MutableLiveData()


     val signInBoolesn : MutableLiveData<Boolean> = MutableLiveData()
     val progressPar : MutableLiveData<Boolean> = MutableLiveData()

    fun getProductByIdFromNetwork(id: Long) {
        repository.getProuduct(id)
    }

    fun observeProductDetails() = repository.prouductDetaild

    fun saveWishList(wishItem: Product) {
        saveWishListJob = CoroutineScope(Dispatchers.IO).launch {
            repository.saveWishList(wishItem)
        }
    }

    fun deleteOneWishItem(id: Long) {
        deleteOneWishItemJob = CoroutineScope(Dispatchers.IO).launch {
            repository.deleteOneWishItem(id)
        }
    }

    fun getOneWithItemFromRoom(id: Long) = repository.getOneWithItem(id)


    fun saveCartList(cartItem: ProductCartModule) {
        if (isSignin()) {
            saveToCartJob = CoroutineScope(Dispatchers.IO).launch {
                repository.saveCartList(cartItem)
            }
        }else{
            signInBoolesn.value = true
        }
    }

    private fun isSignin(): Boolean {
        return true
    }


    override fun onCleared() {
        saveWishListJob?.cancel()
        deleteOneWishItemJob?.cancel()
        saveToCartJob?.cancel()
    }
}