package com.example.shopy.ui.customerAddress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.models.Addresse
import com.example.shopy.models.CreateAddress
import com.example.shopy.models.CreateAddressX
import com.example.shopy.models.UpdateAddresse
import kotlinx.coroutines.launch
import timber.log.Timber

class AddressViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {

    private val postCustomerAddress = SingleLiveEvent<Addresse?>()
    private val defaultCustomerAddress = SingleLiveEvent<Addresse?>()
    private val customerAddresses = SingleLiveEvent<List<Addresse>?>()
    private val delListener = SingleLiveEvent<Pair<Addresse?, Int>>()
    private val navigateListener = SingleLiveEvent<Addresse?>()
    private val dafultListener = SingleLiveEvent<Addresse?>()
    private val editListener = SingleLiveEvent<Addresse?>()

    fun getEditAddress(): LiveData<Addresse?> {
        return editListener
    }


    fun createCustomerAddress(): LiveData<Addresse?> {
        return postCustomerAddress
    }

    fun getdafultAddress(): LiveData<Addresse?> {
         return dafultListener
    }

    fun getAddress(): LiveData<Addresse?> {
        return navigateListener
    }

    fun getDelAddress(): LiveData<Pair<Addresse?, Int>> {
        return delListener
    }


    fun getAddressList(): LiveData<List<Addresse>?> {
        return customerAddresses
    }

    fun onItemClick(addressObj: Addresse) {
        navigateListener.value = addressObj
    }

    fun onCheckBoxClick(addressObj: Addresse) {
        dafultListener.value = addressObj
    }

    fun delItem(addressObj: Addresse, pos: Int) {
        var pair = Pair(addressObj, pos)
        delListener.value = pair
    }
     fun getCustomerAddress( id: String, addressID: String) {
         var data: CreateAddressX? = null
         val jop = viewModelScope.launch {
             data =
                 repository.getCustomerAddress(id, addressID)
         }
         jop.invokeOnCompletion {

             editListener.postValue(data?.address)

             getCustomersAddressList(id)
             Timber.i("olaaa+" + data)
         }
     }
    fun createCustomersAddress(id: String, addressObj: CreateAddress) {
        var data: CreateAddressX? = null
        val jop = viewModelScope.launch {
            data =
                repository.createCustomerAddress(id, addressObj)

        }
        jop.invokeOnCompletion {
            postCustomerAddress.postValue(data?.address)
            getCustomersAddressList(id)
            Timber.i("olaaa+" + data)
        }
    }

    fun getCustomersAddressList(id: String) {
        var data: List<Addresse>? = null
        val jop = viewModelScope.launch {
            data = repository.getCustomerAddresses(id)
        }
        jop.invokeOnCompletion {
            customerAddresses.postValue(data)

            Timber.i("olaaa+" + data)
        }
    }

    fun updateCustomerAddresses(id: String, addressID: String, customerAddress: UpdateAddresse){
         var data: CreateAddressX? = null
         val jop = viewModelScope.launch {
             data =
                 repository.updateCustomerAddresses(id, addressID, customerAddress)
         }
         jop.invokeOnCompletion {

             postCustomerAddress.postValue(data?.address)

             getCustomersAddressList(id)
             Timber.i("olaaa+" + data)
         }
    }

    fun delCustomerAddresses(id: String, addressID: String) {
         viewModelScope.launch {
            repository.delCustomerAddresses(id, addressID)
        }
    }

    fun setDafaultCustomerAddress(id: String, addressID: String) {
        var data: CreateAddressX? = null
        val jop = viewModelScope.launch {
            repository.setDafaultCustomerAddress(id, addressID)
        }
        jop.invokeOnCompletion {
            defaultCustomerAddress.postValue(data?.address)
            getCustomersAddressList(id)
           Timber.i("olaaa+" + data)
        }
    }
}