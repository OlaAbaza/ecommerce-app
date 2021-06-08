package com.example.shopy.dataLayer

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSource
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.datalayer.network.Network
import com.example.shopy.models.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
    val remoteDataSource: RemoteDataSource,
    val roomDataSourceImpl: RoomDataSourceImpl
) {
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()

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
        Network.apiService.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(call: Call<ProductItem?>, response: Response<ProductItem?>) {
                Log.d("TAG","data here")
                prouductDetaild.value = response.body()
            }
            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }


    fun isOnline(context: Context): Boolean {
        return remoteDataSource.isOnline(context)
    }

    fun fetchCatProducts(colID:Long): MutableLiveData<List<Product>> {
       return remoteDataSource.fetchCatProducts(colID)
    }

    fun fetchAllProducts(): MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return remoteDataSource.fetchAllProducts()
    }


     fun getAllOrderList(): LiveData<List<OrderObject>> {
        return  roomDataSourceImpl.getAllOrderList()
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

//     fun getOneWithItem(id: Long): LiveData<com.example.shopy.datalayer.entity.itemPojo.Product> {
//        return roomDataSourceImpl.getOneWithItem(id)
//    }

     suspend fun deleteAllFromCart() {
        roomDataSourceImpl.deleteAllFromCart()
    }

     fun saveAllCartList(dataList: List<ProductCartModule>) {
        roomDataSourceImpl.saveAllCartList(dataList)
    }

    fun getFourWishList() = roomDataSourceImpl.getFourWishList()

    suspend fun deleteOneWishItem(id: Long) = roomDataSourceImpl.deleteOneWithItem(id)

    fun getOneWithItem(id: Long) = roomDataSourceImpl.getOneWithItem(id)

//     fun getAllOrders(): Call<GetOrders>{
//        return remoteDataSource.getAllOrders()
//    }
     fun getAllOrders(): Observable<GetOrders>{
        return remoteDataSource.getAllOrders()
    }
}