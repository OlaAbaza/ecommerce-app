package com.example.shopy.ui.showOneOrderDetails

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.domainLayer.FilterData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler

class ShowOneOrderDetailsViewModel (val repository: Repository, application: Application) :
    AndroidViewModel(application) {
    private val orderLiveData :  MutableLiveData<OneOrderResponce> = MutableLiveData()
    var payNowMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    var cancelMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun getOneOrders(id: Long): LiveData<OneOrderResponce> {
        repository.getOneOrders(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe({ order ->
                orderLiveData.postValue(order)
            }, { error ->

            })
        return orderLiveData
    }


    fun getProductAllProuducts()= repository.getAllProductsList()

    fun deleteOrder(order_id: Long){
        repository.deleteOrder(order_id)
    }
    fun observeDeleteOrder():MutableLiveData<Boolean>{
        return repository.observeDeleteOrder()
    }
}