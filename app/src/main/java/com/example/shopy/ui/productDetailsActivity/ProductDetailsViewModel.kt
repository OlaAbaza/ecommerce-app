package com.example.shopy.ui.productDetailsActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.cartBag.CartRoomRepository
import com.example.shopy.datalayer.localdatabase.room.wishBag.WishRoomRepositry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {
    private val networkingReposatory = RemoteDataSourceImpl()
    private val roomRepositry = WishRoomRepositry(application)
    private var saveWishListJob: Job? = null
    private var deleteOneWishItemJob: Job? = null
    private  var saveToCartJob : Job? = null

     val signInBoolesn : MutableLiveData<Boolean> = MutableLiveData()
     val progressPar : MutableLiveData<Boolean> = MutableLiveData()

    fun getProductByIdFromNetwork(id: Long) {
        networkingReposatory.getProuduct(id)
    }

    fun observeProductDetails() = networkingReposatory.prouductDetaild

    fun saveWishList(wishItem: Product) {
        saveWishListJob = CoroutineScope(Dispatchers.IO).launch {
            roomRepositry.saveWishList(wishItem)
        }
    }

    fun deleteOneWishItem(id: Long) {
        deleteOneWishItemJob = CoroutineScope(Dispatchers.IO).launch {
            roomRepositry.deleteOneWishItem(id)
        }
    }

    fun getOneWithItemFromRoom(id: Long) = roomRepositry.getOneWithItem(id)




    // dealing with cart
    val cartRoom = CartRoomRepository(application)



    fun saveCartList(cartItem: ProductCartModule) {
        if (isSignin()) {
            saveToCartJob = CoroutineScope(Dispatchers.IO).launch {
                cartRoom.saveCartList(cartItem)
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