package com.example.shopy.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.entity.priceRules.PriceRule
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.Addresse


import kotlinx.coroutines.launch

import com.example.shopy.models.Orders
import timber.log.Timber

class OrderViewModel(val repositoryImpl: IRepository, application: Application) :
    AndroidViewModel(application) {
    private val orderDetalis = SingleLiveEvent<Long>()
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

    fun getPostOrder()= repositoryImpl.getCreateOrderResponse()


    fun getdelOrderID(): LiveData<Long> {
        return delOrder
    }
    fun getDetalisOrderID(): LiveData<Long> {
        return orderDetalis
    }

    fun getOrderQuntity(): LiveData<Boolean> {
        return ChangeQuntityListener
    }

    fun insertAllOrder(dataList: List<ProductCartModule>) = repositoryImpl.saveAllCartList(dataList)
    fun getAllCartList() = repositoryImpl.getAllCartList()

    fun delOrder(id: Long) = viewModelScope.launch { repositoryImpl.deleteOnCartItem(id) }
    fun delAllItems() = viewModelScope.launch { repositoryImpl.deleteAllFromCart() }
    fun onDelClick(id: Long) {
        delOrder.postValue(id)
    }
    fun onImgClick(id: Long) {
        orderDetalis.postValue(id)
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
            data = repositoryImpl.getCustomerAddresses(id)
        }
        jop.invokeOnCompletion {
            customerAddresses.postValue(data)

            Timber.i("olaaa+" + data)
        }
    }
    fun saveWishList(wishItem: Product) {
        viewModelScope.launch  {
            repositoryImpl.saveWishList(wishItem)
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
       return  repositoryImpl.getAllDiscountCodeList()
    }
//    fun getallDiscountCodeList(): MutableLiveData<AllCodes> {
//        return repository.getAllDiscountCodes()
//    }

    fun createOrder(order: Orders) = repositoryImpl.createOrder(order)


}