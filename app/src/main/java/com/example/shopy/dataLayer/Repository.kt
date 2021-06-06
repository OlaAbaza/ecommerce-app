package com.example.shopy.dataLayer

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.shopy.dataLayer.localdatabase.room.RoomDataSourceImpl
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSource
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.CreateAddressX
import com.example.shopy.models.OrderResponse
import com.example.shopy.models.Orders

class Repository(
    val remoteDataSource: RemoteDataSource,
    val roomDataSourceImpl: RoomDataSourceImpl
) {

    fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return roomDataSourceImpl.getAllCartList()
    }

    suspend fun saveCartList(withItem: ProductCartModule) {
        roomDataSourceImpl.saveCartList(withItem)
    }

    suspend fun deleteOnCartItem(id: Long) {
        roomDataSourceImpl.deleteOnCartItem(id)
    }


    suspend fun delCustomerAddresses(id: String, addressID: String) {
        remoteDataSource.delCustomerAddresses(id, addressID)
    }

    suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX? {
        return remoteDataSource.setDafaultCustomerAddress(id, addressID)
    }

    suspend fun createOrder(order: Orders): OrderResponse? {
        return remoteDataSource.createOrder(order)
    }

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

    fun isOnline(context: Context): Boolean {
        return remoteDataSource.isOnline(context)
    }

}