package com.example.shopy.data.dataLayer.room

import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule

interface RoomDataSource {

    fun getAllCartList(): LiveData<List<ProductCartModule>>
    suspend fun saveCartList(withItem: ProductCartModule)
    suspend fun deleteOnCartItem(id: Long)
    fun getAllOrderList(): LiveData<List<OrderObject>>
    fun getFourFromWishList(): LiveData<List<Product>>
    fun getAllWishList(): LiveData<List<Product>>
    suspend fun saveWishList(withItem: Product)
    suspend fun deleteOneWithItem(id: Long)
    fun getOneWithItem(id: Long) : LiveData<Product>
    suspend fun deleteAllFromCart()
    fun saveAllCartList(dataList :List<ProductCartModule>)
    suspend fun deleteAllFromWishlist()
    fun getFourWishList(): LiveData<List<Product>>
}