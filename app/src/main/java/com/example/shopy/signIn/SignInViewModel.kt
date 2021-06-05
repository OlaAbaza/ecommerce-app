package com.example.shopy.signIn

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.FirebaseUserLiveData
import com.example.shopy.SingleLiveEvent
import com.example.shopy.dataLayer.RemoteDataSource

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import timber.log.Timber

class SignInViewModel(val remoteDataSource: RemoteDataSource,application: Application) : AndroidViewModel(application) {
    private val customerList = SingleLiveEvent<List<Customer>>()
    private val postResult = SingleLiveEvent<CustomerX?>()
    fun getPostResult(): LiveData<CustomerX?> {
        return postResult
    }

    fun getCustomerList(): LiveData<List<Customer>> {
        return customerList
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun getAllCustomers() {
        viewModelScope.launch {
            var list = remoteDataSource.fetchCustomersData()
            list.let { customerList.postValue(list!!) }
        }
    }

    fun createCustomers(firstName: String, email: String, pass: String) {
        var customer = Customer(firstName, email, pass)
        var customerx :CustomerX?= CustomerX(customer)

        val jop = viewModelScope.launch { customerx =
            customerx?.let { remoteDataSource.createCustomers(it) }
        }
        jop.invokeOnCompletion {
                postResult.postValue(customerx)
                Timber.i("isLoggedjk+" + customerx)

        }
    }
}