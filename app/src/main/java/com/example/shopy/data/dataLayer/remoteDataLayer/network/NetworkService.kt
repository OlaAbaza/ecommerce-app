package com.example.shopy.datalayer.network

import com.example.shopy.data.dataLayer.entity.discount.discountCodes
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.data.dataLayer.entity.priceRules.priceRules
import com.example.shopy.data.dataLayer.itemPojo.Delete
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.models.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface NetworkService {
    @GET("collections/268359631046/products.json")
     fun getWomanProductsList(): Call<ProductsList>

    @GET("collections/268359663814/products.json")
    fun getKidsProductsList(): Call<ProductsList>

    @GET("collections/268359598278/products.json")
    fun getMenProductsList(): Call<ProductsList>

    @GET("collections/268359696582/products.json")
    fun getOnSaleProductsList(): Call<ProductsList>

    @GET("collections/{collection_id}/products.json")
    suspend fun getProducts(@Path("collection_id") collection_id:Long):Response<ProductsList>

    @GET("products.json")
    suspend fun getAllProducts():Response<List<Product>>

    @GET("products.json")
    fun getAllProductsList(): Call<AllProducts>


    //https://ce751b18c7156bf720ea405ad19614f4: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@itiana.myshopify.com/admin/api/2021-04/price_rules/950267576518/discount_codes.json
    @GET("price_rules/950461759686/discount_codes.json")
    fun getAllDiscountCodeList(): Call<AllCodes>

    ///////////////////ola////////////////

    @GET("price_rules.json")
    suspend fun getPriceRulesList():Response<priceRules>
//    @GET("price_rules/950267576518/discount_codes.json")
//    suspend fun getDiscountCodeList():Response<discountCodes>

    @GET("customers.json?limit=250")
    suspend fun getCustomers(): Response<Customers>

    @POST("customers.json")
    suspend fun createCustomer(@Body customer: CustomerX): Response<CustomerX>?

    @POST("customers/{customer_id}/addresses.json")
    suspend fun createCustomerAddress(
        @Path("customer_id") id: String,
        @Body customerAddress: CreateAddress
    ): Response<CreateAddressX>

    @GET("customers/{customer_id}/addresses.json")
    suspend fun getCustomerAddresses(@Path("customer_id") id: String): Response<customerAddresses>

    @GET("customers/{customer_id}.json")
    suspend fun getCustomer(@Path("customer_id") id: String): Response<CustomerX>

    @PUT(" customers/{customer_id}.json")
    suspend fun updateCustomer(
        @Path("customer_id") id: String,
        @Body customer: CustomerProfile
    ): Response<CustomerX>

    @PUT(" customers/{customer_id}/addresses/{address_id}.json")
    suspend fun updateCustomerAddresses(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String,
        @Body customerAddress: UpdateAddresse
    ): Response<CreateAddressX>
    @GET("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun getCustomerAddress(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<CreateAddressX>

    @DELETE(" customers/{customer_id}/addresses/{address_id}.json")
    suspend fun delCustomerAddresses(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<ResponseBody>

    @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun setDafaultCustomerAddress(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String
    ): Response<CreateAddressX>

    @POST("orders.json")
     suspend fun createOrder(
        @Body order: Orders
    ): Response<OneOrderResponce>



///////////////////////////////Esraa////////////////////////////////


    @GET("products/{id}.json")
    fun getOneProduct(@Path("id") id: Long) : Call<ProductItem>


    @GET("orders.json")
    fun getAllOrders() : Observable<GetOrders>

    @GET("orders/{id}.json")
    fun getOneOrders(@Path("id") id: Long) : Observable<OneOrderResponce>

    @DELETE("orders/{id}.json")
    fun deleteOrder(@Path("id")order_id : Long) :Call<Delete>
}