package com.example.shopy.datalayer.onlineDataLayer.productDetailsService

import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductDetailsDao {
    @GET("products/{id}.json")
    fun getOneProduct(@Path("id") id: Long) : Call<ProductItem>
}