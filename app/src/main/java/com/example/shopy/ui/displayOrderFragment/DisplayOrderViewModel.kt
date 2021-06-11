package com.example.shopy.ui.displayOrderFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.domainLayer.FilterData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler

class DisplayOrderViewModel(val repository: Repository, application: Application) :
    AndroidViewModel(application) {
    lateinit var disposable: Disposable
    var orders: MutableLiveData<List<GetOrders.Order?>> = MutableLiveData()
    var payNowMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    var cancelMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()
    var deleteOrder: MutableLiveData<Boolean> = MutableLiveData()


    fun getPaidOrders( userId: Long) {
        disposable = repository.getAllOrders().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe({ vehicles ->
                orders.postValue(
                    FilterData.getPaidData(
                        FilterData.getAllData(vehicles.orders!!, userId)
                    )
                )

            }, { error ->
                this.error.postValue(true)
            })

    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    fun getUnPaidOrders(userId: Long) {

        disposable = repository.getAllOrders().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe({ vehicles ->
                orders.postValue(
                    FilterData.getUnPaidData(
                        FilterData.getAllData(vehicles.orders!!, userId)
                    )
                )
            }, { error ->
                this.error.postValue(true)
            })
    }

    fun deleteOrder(order_id: Long): MutableLiveData<Boolean> {
      deleteOrder =   repository.deleteOrder(order_id)
        return deleteOrder
    }

}