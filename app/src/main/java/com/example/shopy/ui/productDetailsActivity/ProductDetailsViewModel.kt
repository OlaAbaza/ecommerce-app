package com.example.shopy.ui.productDetailsActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val repositoryImpl: IRepository, application: Application) : AndroidViewModel(application) {

    private var saveWishListJob: Job? = null
    private var deleteOneWishItemJob: Job? = null
    private  var saveToCartJob : Job? = null
    var optionsMutableLiveData : MutableLiveData<Int> = MutableLiveData()


     val signInBoolesn : MutableLiveData<Boolean> = MutableLiveData()
     val progressPar : MutableLiveData<Boolean> = MutableLiveData()

    fun getProductByIdFromNetwork(id: Long) {
        repositoryImpl.getProuduct(id)
    }

    fun observeProductDetails() = repositoryImpl.prouductDetaild

    fun saveWishList(wishItem: Product) {
        saveWishListJob = CoroutineScope(Dispatchers.IO).launch {
            repositoryImpl.saveWishList(wishItem)
        }
    }

    fun deleteOneWishItem(id: Long) {
        deleteOneWishItemJob = CoroutineScope(Dispatchers.IO).launch {
            repositoryImpl.deleteOneWishItem(id)
        }
    }

    fun getOneWithItemFromRoom(id: Long) = repositoryImpl.getOneWithItem(id)


    fun saveCartList(cartItem: ProductCartModule) {
        if (isSignin()) {
            saveToCartJob = CoroutineScope(Dispatchers.IO).launch {
                repositoryImpl.saveCartList(cartItem)
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