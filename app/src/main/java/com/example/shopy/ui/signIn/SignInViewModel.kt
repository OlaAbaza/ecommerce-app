package com.example.shopy.ui.signIn

import android.app.Application
import androidx.lifecycle.*
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl

import kotlinx.coroutines.launch

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import timber.log.Timber

class SignInViewModel(val repositoryImpl: IRepository, application: Application) : AndroidViewModel(application) {
    private val customerList = SingleLiveEvent<List<Customer>>()
    private val postResult = SingleLiveEvent<CustomerX?>()

    //for testing
//      init {
//        getAllCustomers()
//        createCustomers("ola","ola2@gmail.com","12345")
//     }

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
            var list = repositoryImpl.fetchCustomersData()
            list.let { customerList.postValue(list!!) }
        }
    }

    fun createCustomers(firstName: String, email: String, pass: String) {
        var customer = Customer(firstName, email, pass)
        var customerx :CustomerX?= CustomerX(customer)

        val jop = viewModelScope.launch { customerx =
            customerx?.let { repositoryImpl.createCustomers(it) }
        }
        jop.invokeOnCompletion {
                postResult.postValue(customerx)
                Timber.i("isLogged" + customerx)

        }
    }
}