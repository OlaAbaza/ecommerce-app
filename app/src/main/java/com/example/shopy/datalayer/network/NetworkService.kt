package com.example.shopy.datalayer.network

import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import retrofit2.Call
import retrofit2.http.GET


interface NetworkService {
    @GET("collections/268359631046/products.json")
     fun getWomanProductsList(): Call<ProductsList>

    @GET("collections/268359663814/products.json")
    fun getKidsProductsList(): Call<ProductsList>

    @GET("collections/268359598278/products.json")
    fun getMenProductsList(): Call<ProductsList>

    @GET("collections/268359696582/products.json")
    fun getOnSaleProductsList(): Call<ProductsList>

    @GET("products.json")
    fun getAllProductsList(): Call<AllProducts>

    //https://ce751b18c7156bf720ea405ad19614f4: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@itiana.myshopify.com/admin/api/2021-04/price_rules/950267576518/discount_codes.json
    @GET("price_rules/950461759686/discount_codes.json")
    fun getAllDiscountCodeList(): Call<AllCodes>
}