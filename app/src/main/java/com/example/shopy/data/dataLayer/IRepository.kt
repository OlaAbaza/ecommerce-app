package com.example.shopy.data.dataLayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.base.SingleLiveEvent
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSource
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.models.*
import io.reactivex.Observable

interface IRepository {
    val remoteDataSource: RemoteDataSource
    val roomDataSourceImpl: RoomDataSourceImpl
    var prouductDetaild: MutableLiveData<ProductItem>

    ////////////////////cart list////////////////
    fun getAllCartList(): LiveData<List<ProductCartModule>>

    suspend fun saveCartList(withItem: ProductCartModule)

    suspend fun deleteOnCartItem(id: Long)

    suspend fun deleteAllFromCart()

    suspend fun deleteAllFromWish()
    fun saveAllCartList(dataList: List<ProductCartModule>)

    /////////////customer address/////////////////
    suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress): CreateAddressX?

    suspend fun getCustomerAddresses(id: String): List<Addresse>?

    suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ): CreateAddressX?

    suspend fun delCustomerAddresses(id: String, addressID: String)

    suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX?

    suspend fun getCustomerAddress(id: String, addressID: String): CreateAddressX?

    ////////////////////customer///////////////////
    suspend fun fetchCustomersData(): List<Customer>?

    suspend fun createCustomers(customer: CustomerX): CustomerX?

    suspend fun getCustomer(id: String): CustomerX?

    suspend fun updateCustomer(id: String, customer: CustomerProfile): CustomerX?

    /////////////////////create order/////////////////////////////
    fun createOrder(order: Orders)
    fun getCreateOrderResponse(): SingleLiveEvent<OneOrderResponce?>

    ///////////////////products/////////////////////////
    fun getWomanProductsList(): MutableLiveData<ProductsList>
    fun getKidsProductsList(): MutableLiveData<ProductsList>
    fun getMenProductsList(): MutableLiveData<ProductsList>
    fun getOnSaleProductsList(): MutableLiveData<ProductsList>
    fun getAllProductsList(): MutableLiveData<AllProducts>
    fun getAllDiscountCodeList(): MutableLiveData<AllCodes>
    fun getProuduct(id: Long)
    fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>>
    fun fetchAllProducts(): MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>
    fun getAllOrderList(): LiveData<List<OrderObject>>

    // wish list
    fun getFourFromWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>
    fun getAllWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>

    suspend fun saveWishList(withItem: com.example.shopy.datalayer.entity.itemPojo.Product)

    suspend fun deleteOneWithItem(id: Long)
    fun getFourWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>

    suspend fun deleteOneWishItem(id: Long)
    fun getOneWithItem(id: Long): LiveData<com.example.shopy.datalayer.entity.itemPojo.Product>

    //     fun getAllOrders(): Call<GetOrders>{
//        return remoteDataSource.getAllOrders()
//    }
    fun getAllOrders(): Observable<GetOrders>
    fun deleteOrder(order_id: Long)
    fun observeDeleteOrder(): MutableLiveData<Boolean>
    fun getOneOrders(id: Long): Observable<OneOrderResponce>
}