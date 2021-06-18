package com.example.shopy.ui.showOneOrderDetails

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler

class ShowOneOrderDetailsViewModel (val repositoryImpl: IRepository, application: Application) :
    AndroidViewModel(application) {
    private val orderLiveData :  MutableLiveData<OneOrderResponce> = MutableLiveData()
    var payNowMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    var cancelMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun getOneOrders(id: Long): LiveData<OneOrderResponce> {
        repositoryImpl.getOneOrders(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe({ order ->
                orderLiveData.postValue(order)
            }, { error ->

            })
        return orderLiveData
    }


    fun getProductAllProuducts()= repositoryImpl.getAllProductsList()

    fun deleteOrder(order_id: Long){
        repositoryImpl.deleteOrder(order_id)
    }
    fun observeDeleteOrder():MutableLiveData<Boolean>{
        return repositoryImpl.observeDeleteOrder()
    }
}