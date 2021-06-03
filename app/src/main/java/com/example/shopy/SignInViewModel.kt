package com.example.shopy

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.RemoteDataSource

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import timber.log.Timber

class SignInViewModel(val remoteDataSource: RemoteDataSource, application: Application) : AndroidViewModel(application) {
    private val customerList = SingleLiveEvent<List<Customer>>()
    private val postResult = SingleLiveEvent<String>()
    fun getPostResult(): LiveData<String> {
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
        var customerx = CustomerX(customer)
        var result = ""
        val jop = viewModelScope.launch { result = remoteDataSource.createCustomers(customerx) }
        jop.invokeOnCompletion {
            postResult.postValue(result)
            Timber.i("isLoggedjk+" + result)
        }
    }
}