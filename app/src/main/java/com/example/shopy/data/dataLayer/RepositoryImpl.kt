package com.example.shopy.data.dataLayer

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSource
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.entity.itemPojo.OrderObject
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.*
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.datalayer.network.Network
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryImpl(
    override val remoteDataSource: RemoteDataSource,
    override val roomDataSourceImpl: RoomDataSourceImpl
) : IRepository {
    override var prouductDetaild: MutableLiveData<ProductItem> = MutableLiveData()

    ////////////////////cart list////////////////
    override fun getAllCartList(): LiveData<List<ProductCartModule>> {
        return roomDataSourceImpl.getAllCartList()
    }

    override suspend fun saveCartList(withItem: ProductCartModule) {
        roomDataSourceImpl.saveCartList(withItem)
    }

    override suspend fun deleteOnCartItem(id: Long) {
        roomDataSourceImpl.deleteOnCartItem(id)
    }

    override suspend fun deleteAllFromCart() {
        roomDataSourceImpl.deleteAllFromCart()
    }

    override suspend fun deleteAllFromWish() {
        roomDataSourceImpl.deleteAllFromWishlist()
    }

    override fun saveAllCartList(dataList: List<ProductCartModule>) {
        roomDataSourceImpl.saveAllCartList(dataList)
    }

    /////////////customer address/////////////////
    override suspend fun createCustomerAddress(id: String, customerAddress: CreateAddress) =
        remoteDataSource.createCustomerAddress(id, customerAddress)

    override suspend fun getCustomerAddresses(id: String) = remoteDataSource.getCustomerAddresses(id)
    override suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ) = remoteDataSource.updateCustomerAddresses(id, addressID, customerAddress)

    override suspend fun delCustomerAddresses(id: String, addressID: String) =
        remoteDataSource.delCustomerAddresses(id, addressID)


    override suspend fun setDafaultCustomerAddress(id: String, addressID: String) =
        remoteDataSource.setDafaultCustomerAddress(id, addressID)

    override suspend fun getCustomerAddress(id: String, addressID: String) =
        remoteDataSource.getCustomerAddress(id, addressID)

    ////////////////////customer///////////////////
    override suspend fun fetchCustomersData() = remoteDataSource.fetchCustomersData()
    override suspend fun createCustomers(customer: CustomerX) = remoteDataSource.createCustomers(customer)
    override suspend fun getCustomer(id: String) = remoteDataSource.getCustomer(id)
    override suspend fun updateCustomer(id: String, customer: CustomerProfile) =
        remoteDataSource.updateCustomer(id, customer)

    /////////////////////create order/////////////////////////////
    override fun createOrder(order: Orders) =
        remoteDataSource.createOrder(order)

    override fun getCreateOrderResponse() = remoteDataSource.getCreateOrderResponse()
    // suspend fun getPriceRulesList()=remoteDataSource.getPriceRulesList()

    ///////////////////products/////////////////////////
    override fun getWomanProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getWomanProductsList()
    }

    override fun getKidsProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getKidsProductsList()
    }

    override fun getMenProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getMenProductsList()
    }

    override fun getOnSaleProductsList(): MutableLiveData<ProductsList> {
        return remoteDataSource.getOnSaleProductsList()
    }

    override fun getAllProductsList(): MutableLiveData<AllProducts> {
        return remoteDataSource.getAllProductsList()
    }

    override fun getAllDiscountCodeList(): MutableLiveData<AllCodes> {
        return remoteDataSource.getAllDiscountCodeList()
    }
    // fun getAllDiscountCodes() = remoteDataSource.getAllDiscountCodesData()


    override fun getProuduct(id: Long) {
        remoteDataSource.getProuduct(id)
        Network.apiService.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(
                call: Call<ProductItem?>,
                response: Response<ProductItem?>
            ) {
                Log.d("TAG", "data here")
                prouductDetaild.value = response.body()
            }

            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }


    override fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>> {
        return remoteDataSource.fetchCatProducts(colID)
    }

    override fun fetchAllProducts(): MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return remoteDataSource.fetchAllProducts()
    }
/////////////////////////////////////////////////////////////////////

    override fun getAllOrderList(): LiveData<List<OrderObject>> {
        return roomDataSourceImpl.getAllOrderList()
    }


    // wish list
    override fun getFourFromWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return roomDataSourceImpl.getFourFromWishList()
    }

    override fun getAllWishList(): LiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        return roomDataSourceImpl.getAllWishList()
    }

    override suspend fun saveWishList(withItem: com.example.shopy.datalayer.entity.itemPojo.Product) {
        roomDataSourceImpl.saveWishList(withItem)
    }

    override suspend fun deleteOneWithItem(id: Long) {
        roomDataSourceImpl.deleteOneWithItem(id)
    }


//    fun getOneWithItem(id: Long): LiveData<com.example.shopy.datalayer.entity.itemPojo.Product> {
//        return roomDataSourceImpl.getOneWithItem(id)
//    }

//     fun getOneWithItem(id: Long): LiveData<com.example.shopy.datalayer.entity.itemPojo.Product> {
//        return roomDataSourceImpl.getOneWithItem(id)
//    }


    override fun getFourWishList() = roomDataSourceImpl.getFourWishList()

    /////////////////////////////////is online//////////

    override suspend fun deleteOneWishItem(id: Long) = roomDataSourceImpl.deleteOneWithItem(id)

    override fun getOneWithItem(id: Long) = roomDataSourceImpl.getOneWithItem(id)

    //     fun getAllOrders(): Call<GetOrders>{
//        return remoteDataSource.getAllOrders()
//    }
    override fun getAllOrders(): Observable<GetOrders> {
        return remoteDataSource.getAllOrders()
    }

    override fun deleteOrder(order_id: Long) {
        // remoteDataSource.deleteOrder(order_id)
        remoteDataSource.deleteOrder(order_id)
    }

    override fun observeDeleteOrder() = remoteDataSource.observeDeleteOrder()

    override fun getOneOrders(id: Long): Observable<OneOrderResponce> {
       return remoteDataSource.getOneOrders(id)
    }
}