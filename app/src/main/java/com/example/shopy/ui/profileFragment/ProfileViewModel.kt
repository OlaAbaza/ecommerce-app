package com.example.shopy.ui.profileFragment


import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerProfile
import com.example.shopy.models.CustomerX
import timber.log.Timber

class ProfileViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {
    private val customerResponse= SingleLiveEvent<Customer>()
    private val postResult = SingleLiveEvent<CustomerX?>()
    fun getPostResult(): LiveData<CustomerX?> {
        return postResult
    }

    fun getCustomerInfo(): LiveData<Customer> {
        return customerResponse
    }


    fun getCustomer(id:String) {
        viewModelScope.launch {
            var customer = repository.getCustomer(id)
            customer.let { customerResponse.postValue(customer?.customer) }
            Timber.i("ola customer"+customer)
        }
     }

    fun UpdateCustomers(id:String,item:CustomerProfile) {

        var customer = item.customer?.firstName?.let { Customer(it,"null", "null") }
        var customerx :CustomerX?= customer?.let { CustomerX(it) }

        val jop = viewModelScope.launch {
            customerx = repository.updateCustomer(id,item)
        }
        jop.invokeOnCompletion {
            postResult.postValue(customerx)
            Timber.i("ola customer1+" + customerx)

        }
    }
}