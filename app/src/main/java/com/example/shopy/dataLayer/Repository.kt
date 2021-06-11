package com.example.shopy.dataLayer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSource
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.*

class Repository(
    val remoteDataSource: RemoteDataSource,
    val roomDataSourceImpl: RoomDataSourceImpl
) {

    ////////////////////cart list////////////////
    fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return roomDataSourceImpl.getAllCartList()
    }

    suspend fun saveCartList(withItem: ProductCartModule) {
        roomDataSourceImpl.saveCartList(withItem)
    }

    suspend fun deleteOnCartItem(id: Long) {
        roomDataSourceImpl.deleteOnCartItem(id)
    }
    suspend fun deleteAllFromCart() {
        roomDataSourceImpl.deleteAllFromCart()
    }

    fun saveAllCartList(dataList: List<ProductCartModule>) {
        roomDataSourceImpl.saveAllCartList(dataList)
    }

    /////////////customer address/////////////////
    suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress) =
        remoteDataSource.createCustomerAddress(id, customerAddress)

    suspend fun getCustomerAddresses(id: String) = remoteDataSource.getCustomerAddresses(id)
    suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ) = remoteDataSource.updateCustomerAddresses(id, addressID, customerAddress)

    suspend fun delCustomerAddresses(id: String, addressID: String) =
        remoteDataSource.delCustomerAddresses(id, addressID)


    suspend fun setDafaultCustomerAddress(id: String, addressID: String) =
        remoteDataSource.setDafaultCustomerAddress(id, addressID)
     suspend fun getCustomerAddress( id: String, addressID: String)=remoteDataSource.getCustomerAddress(id,addressID)

    ////////////////////customer///////////////////
    suspend fun fetchCustomersData() = remoteDataSource.fetchCustomersData()
    suspend fun createCustomers(customer: CustomerX) = remoteDataSource.createCustomers(customer)
    suspend fun getCustomer(id: String)=remoteDataSource.getCustomer(id)
    suspend fun updateCustomer(id: String, customer: CustomerProfile)=remoteDataSource.updateCustomer(id,customer)

    /////////////////////create order/////////////////////////////
    suspend fun createOrder(order: Orders) = remoteDataSource.createOrder(order)

///////////////////products/////////////////////////
    fun getWomanProductsList() {
        remoteDataSource.getWomanProductsList()
    }

    fun getKidsProductsList() {
        remoteDataSource.getKidsProductsList()
    }

    fun getMenProductsList() {
        remoteDataSource.getMenProductsList()
    }

    fun getOnSaleProductsList() {
        remoteDataSource.getOnSaleProductsList()
    }

    fun getAllProductsList() {
        remoteDataSource.getAllProductsList()
    }

    fun getAllDiscountCodeList() {
        remoteDataSource.getAllDiscountCodeList()
    }

    fun getProuduct(id: Long) {
        remoteDataSource.getProuduct(id)
    }


    fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>> {
        return remoteDataSource.fetchCatProducts(colID)
    }

    fun fetchAllProducts(): MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return remoteDataSource.fetchAllProducts()
    }
/////////////////////////////////////////////////////////////////////

    fun getAllOrderList(): LiveData<List<OrderObject>> {
        return roomDataSourceImpl.getAllOrderList()
    }


    // wish list
    fun getFourFromWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return roomDataSourceImpl.getFourFromWishList()
    }

    fun getAllWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return roomDataSourceImpl.getAllWishList()
    }

    suspend fun saveWishList(withItem: com.example.shopy.datalayer.entity.itemPojo.Product) {
        roomDataSourceImpl.saveWishList(withItem)
    }

    suspend fun deleteOneWithItem(id: Long) {
        roomDataSourceImpl.deleteOneWithItem(id)
    }

    fun getOneWithItem(id: Long): LiveData<com.example.shopy.datalayer.entity.itemPojo.Product> {
        return roomDataSourceImpl.getOneWithItem(id)
    }

    fun getFourWishList() = roomDataSourceImpl.getFourWishList()
    /////////////////////////////////is online//////////
    fun isOnline(context: Context): Boolean {
        return remoteDataSource.isOnline(context)
    }
}