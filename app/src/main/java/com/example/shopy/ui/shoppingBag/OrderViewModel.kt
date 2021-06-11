package com.example.shopy.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.Addresse


import kotlinx.coroutines.launch

import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders
import timber.log.Timber

class OrderViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {
    private val postOrder = SingleLiveEvent<OrderResponse?>()
    private val delOrder = SingleLiveEvent<Long>()
    private val ChangeQuntityListener = SingleLiveEvent<Boolean>()
    private val customerAddresses = SingleLiveEvent<List<Addresse>?>()

    fun getAddressList(): LiveData<List<Addresse>?> {
        return customerAddresses
    }
    fun getPostOrder(): LiveData<OrderResponse?> {
        return postOrder
    }
    fun getdelOrderID(): LiveData<Long> {
        return delOrder
    }
    fun getOrderQuntity(): LiveData<Boolean> {
        return ChangeQuntityListener
    }
    fun insertAllOrder(dataList: List<ProductCartModule>) = repository.saveAllCartList(dataList)
    fun getAllCartList() = repository.getAllCartList()

    fun delOrder(id:Long) = viewModelScope.launch{repository.deleteOnCartItem(id)}
    fun delAllItems()=viewModelScope.launch{repository.deleteAllFromCart()}
    fun onDelClick(id:Long){
        delOrder.postValue(id)
    }
    fun onChangeQuntity(){
        ChangeQuntityListener.postValue(true)
    }
    fun getCustomersAddressList(id: String) {
        var data: List<Addresse>? = null
        val jop = viewModelScope.launch {
            data = repository.getCustomerAddresses(id)
        }
        jop.invokeOnCompletion {
            customerAddresses.postValue(data)

            Timber.i("olaaa+" + data)
        }
    }


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