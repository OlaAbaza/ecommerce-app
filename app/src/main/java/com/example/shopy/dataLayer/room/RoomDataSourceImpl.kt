package com.example.shopy.dataLayer.room

import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.localdatabase.room.cartBag.CartDao
import com.example.shopy.datalayer.localdatabase.room.order.OrderDao
import com.example.shopy.datalayer.localdatabase.room.wishBag.WishDao

class RoomDataSourceImpl(database: RoomService?) : RoomDataSource {

    val cartDao: CartDao = database!!.caerDao()
    val orderDao : OrderDao = database!!.orderDao()
    val wishDao : WishDao = database!!.wishDao()


    // cart

    override fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return cartDao.getAllCartList()
    }

    override suspend fun saveCartList(withItem: ProductCartModule) {
        cartDao.saveCartList(withItem)
    }

    override suspend fun deleteOnCartItem(id: Long) {
        cartDao.deleteOnCartItem(id)
    }


    // order

    override fun getAllOrderList(): LiveData<List<OrderObject>> {
        return  orderDao.getAllOrderList()
    }









    // wish list
    override fun getFourFromWishList(): LiveData<List<Product>> {
        return wishDao.getFourFromWishList()
    }

    override fun getAllWishList(): LiveData<List<Product>> {
        return wishDao.getAllWishList()
    }

    override suspend fun saveWishList(withItem: Product) {
        wishDao.saveWishList(withItem)
    }

    override suspend fun deleteOneWithItem(id: Long) {
        wishDao.deleteOneWithItem(id)
    }

    override fun getOneWithItem(id: Long): LiveData<Product> {
        return wishDao.getOneWithItem(id)
    }

    override suspend fun deleteAllFromCart() {
        cartDao.deleteAllFromCart()
    }

    override fun saveAllCartList(dataList: List<ProductCartModule>) {
        cartDao.saveAllCartList(dataList)
    }

    override fun getFourWishList(): LiveData<List<Product>> {
        return wishDao.getFourFromWishList()
    }
}