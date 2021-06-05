package com.example.shopy.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.datalayer.RemoteDataSourceImpl

import kotlinx.coroutines.launch

import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders
import timber.log.Timber

class OrderViewModel(val remoteDataSource: RemoteDataSourceImpl,application: Application) : AndroidViewModel(application) {
    private val postOrder = SingleLiveEvent<OrderResponse?>()
    fun getPostOrder(): LiveData<OrderResponse?> {
        return postOrder
    }


    fun createOrder(order: Orders) {
        var orderResponse: OrderResponse? = null
        val jop = viewModelScope.launch { orderResponse =
           remoteDataSource.createOrder(order)
        }
        jop.invokeOnCompletion {
            orderResponse?.let {
                postOrder.postValue(it)
            }

            Timber.i("orderResponse" + orderResponse)

        }
    }
}