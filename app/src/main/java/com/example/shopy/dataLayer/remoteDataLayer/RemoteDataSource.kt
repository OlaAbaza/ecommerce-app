package com.example.shopy.dataLayer.remoteDataLayer


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path


interface RemoteDataSource {
    suspend fun fetchCustomersData(): List<Customer>?
    suspend fun createCustomers(customer: CustomerX): CustomerX?
    suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress): CreateAddressX?
    suspend fun getCustomerAddresses(id: String): List<Addresse>?
    suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ): CreateAddressX?

    suspend fun delCustomerAddresses(id: String, addressID: String)
    suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX?
    suspend fun createOrder(order: Orders): OrderResponse?

     fun getWomanProductsList()
     fun getKidsProductsList()
     fun getMenProductsList()
     fun getOnSaleProductsList()
     fun getAllProductsList()
     fun getAllDiscountCodeList()

    fun getProuduct(id : Long)

    fun isOnline(context: Context): Boolean

    fun fetchCatProducts(colID:Long):MutableLiveData<List<Product>>
    fun fetchAllProducts():MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>

}
