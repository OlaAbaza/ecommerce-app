package com.example.shopy.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.dataLayer.Repository
import com.example.shopy.datalayer.localdatabase.room.cartBag.CartRoomRepository

import kotlinx.coroutines.launch

import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders
import timber.log.Timber

class OrderViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {
    private val postOrder = SingleLiveEvent<OrderResponse?>()
    private val cartRoom  = CartRoomRepository(application)
    fun getPostOrder(): LiveData<OrderResponse?> {
        return postOrder
    }

    fun getAllCartList() = repository.getAllCartList()



    fun createOrder(order: Orders) {
        var orderResponse: OrderResponse? = null
        val jop = viewModelScope.launch { orderResponse =
           repository.createOrder(order)
        }
        jop.invokeOnCompletion {
            orderResponse?.let {
                postOrder.postValue(it)
            }

            Timber.i("orderResponse%s", orderResponse)

        }
    }
}