package com.example.shopy.data.dataLayer.remoteDataLayer


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.models.*
import io.reactivex.Observable


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
    suspend fun getCustomerAddress(id: String, addressID: String): CreateAddressX?

    suspend fun delCustomerAddresses(id: String, addressID: String)
    suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX?

    suspend fun getCustomer( id: String): CustomerX?

    suspend fun updateCustomer(id: String, customer: CustomerProfile): CustomerX?
    fun createOrder(order: Orders)
    fun getCreateOrderResponse(): SingleLiveEvent<OneOrderResponce?>

     //shopTab
     fun getWomanProductsList() : MutableLiveData<ProductsList>
     fun getKidsProductsList() : MutableLiveData<ProductsList>
     fun getMenProductsList() : MutableLiveData<ProductsList>
     fun getOnSaleProductsList() : MutableLiveData<ProductsList>
     fun getAllProductsList() : MutableLiveData<AllProducts>
     fun getAllDiscountCodeList() : MutableLiveData<AllCodes>

    fun getProuduct(id : Long)

    fun fetchCatProducts(colID:Long):MutableLiveData<List<Product>>
    fun fetchAllProducts():MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>

   // fun  getAllDiscountCodesData(): MutableLiveData<AllCodes>
   // suspend fun getDiscountCodeList():discountCodes?
    //suspend fun getPriceRulesList(): priceRules?
    fun getAllOrders() : Observable<GetOrders>

    fun deleteOrder(order_id : Long)
    fun observeDeleteOrder():MutableLiveData<Boolean>

    fun getOneOrders(id: Long) : Observable<OneOrderResponce>


}
