package com.example.shopy

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.SingleLiveEvent
import com.example.shopy.dataLayer.RemoteDataSource

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders
import timber.log.Timber

class OrderViewModel(val remoteDataSource: RemoteDataSource,application: Application) : AndroidViewModel(application) {
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