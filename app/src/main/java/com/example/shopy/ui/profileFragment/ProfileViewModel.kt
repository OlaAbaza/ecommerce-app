package com.example.shopy.ui.profileFragment


import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerProfile
import com.example.shopy.models.CustomerX
import com.example.shopy.models.CustomerXXX
import timber.log.Timber

class ProfileViewModel(val repositoryImpl: IRepository, application: Application) : AndroidViewModel(application) {
    private val customerResponse= SingleLiveEvent<Customer>()
    private val postResult = SingleLiveEvent<CustomerX?>()
    fun getPostResult(): LiveData<CustomerX?> {
        return postResult
    }

    fun getCustomerInfo(): LiveData<Customer> {
        return customerResponse
    }
//init{
//    var customerxxx = CustomerXXX("ola",1234, "null","01023657698")
//    var customerProfile = CustomerProfile(customerxxx)
//    getCustomer("123456789")
//    UpdateCustomers("1234567",customerProfile)
//}

    fun getCustomer(id:String) {
        viewModelScope.launch {
            var customer = repositoryImpl.getCustomer(id)
            customer.let { customerResponse.postValue(customer?.customer) }
            Timber.i("ola customer"+customer)
        }
     }

    fun UpdateCustomers(id:String,item:CustomerProfile) {

        var customer = item.customer?.firstName?.let { Customer(it,"null", "null") }
        var customerx :CustomerX?= customer?.let { CustomerX(it) }

        val jop = viewModelScope.launch {
            customerx = repositoryImpl.updateCustomer(id,item)
        }
        jop.invokeOnCompletion {
            postResult.postValue(customerx)
            Timber.i("ola customer1+" + customerx)

        }
    }
}