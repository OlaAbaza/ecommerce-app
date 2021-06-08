package com.example.shopy.ui.displayOrderFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.datalayer.network.Network
import com.example.shopy.domainLayer.FilterData
import com.example.shopy.models.OrderResponse
import com.example.shopy.models.OrdersGetResponse
import com.example.shopy.models.OrdersResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import retrofit2.Call

class DisplayOrderViewModel(val repository: Repository, application: Application) :
    AndroidViewModel(application) {
    lateinit var disposable: Disposable
    var orders: MutableLiveData<List<GetOrders.Order?>> = MutableLiveData()
    var payNowMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()
    var cancelMutableData: MutableLiveData<GetOrders.Order> = MutableLiveData()

    fun getAllOrders() {
        disposable = repository.getAllOrders().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe({ vehicles ->
                Log.d("TAG", "vechicles ${vehicles.orders!!.count()}")
// orders.postValue(vehicles.orders)
                orders.postValue(FilterData.getAllData(vehicles.orders!!))
                Log.d("TAG", "orders.value ${orders.value!!.size}")

            }, { error ->
                Log.d("TAG", error.printStackTrace().toString())
            })

    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}