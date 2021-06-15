package com.example.shopy.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.entity.priceRules.PriceRule
import com.example.shopy.data.dataLayer.entity.priceRules.priceRules
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.Addresse


import kotlinx.coroutines.launch

import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class OrderViewModel(val repository: Repository, application: Application) :
    AndroidViewModel(application) {
    private val delOrder = SingleLiveEvent<Long>()
    private val favOrder = SingleLiveEvent<ProductCartModule>()
    private val ChangeQuntityListener = SingleLiveEvent<Boolean>()
    private val customerAddresses = SingleLiveEvent<List<Addresse>?>()
    private val priceRules = SingleLiveEvent<List<PriceRule>?>()

    fun getPriceRules(): LiveData<List<PriceRule>?> {
        return priceRules
    }
    fun getFavOrder(): LiveData<ProductCartModule> {
        return favOrder
    }
    fun getAddressList(): LiveData<List<Addresse>?> {
        return customerAddresses
    }

    fun getPostOrder(): LiveData<Boolean> {
        return repository.getCreateOrderResponse()
    }

    fun getdelOrderID(): LiveData<Long> {
        return delOrder
    }

    fun getOrderQuntity(): LiveData<Boolean> {
        return ChangeQuntityListener
    }

    fun insertAllOrder(dataList: List<ProductCartModule>) = repository.saveAllCartList(dataList)
    fun getAllCartList() = repository.getAllCartList()

    fun delOrder(id: Long) = viewModelScope.launch { repository.deleteOnCartItem(id) }
    fun delAllItems() = viewModelScope.launch { repository.deleteAllFromCart() }
    fun onDelClick(id: Long) {
        delOrder.postValue(id)
    }
    fun onFavClick(item: ProductCartModule) {
        favOrder.postValue(item)
    }

    fun onChangeQuntity() {
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
    fun saveWishList(wishItem: Product) {
        viewModelScope.launch  {
            repository.saveWishList(wishItem)
        }
    }

    //    fun getPriceRulesList(){
//        var data: priceRules? = null
//        val jop = viewModelScope.launch {
//            data = repository.getPriceRulesList()
//        }
//        jop.invokeOnCompletion {
//            priceRules.postValue(data?.priceRules)
//
//            Timber.i("olaaa+" + data)
//        }
//    }
    fun fetchallDiscountCodeList(): MutableLiveData<AllCodes> {
       return  repository.getAllDiscountCodeList()
    }
//    fun getallDiscountCodeList(): MutableLiveData<AllCodes> {
//        return repository.getAllDiscountCodes()
//    }

    fun createOrder(order: Orders) = repository.createOrder(order)


}