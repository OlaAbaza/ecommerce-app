package com.example.shopy

import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
import com.example.shopy.models.Customers
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {

    @GET("customers.json")
    suspend fun getCustomers():Response<Customers>

    @POST("customers.json")
    suspend fun createCustomer(@Body customer: CustomerX): Response<CustomerX>?
}
//https://ce751b18c7156bf720ea405ad19614f4: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@itiana.myshopify.com/
// admin/api/2021-04/customers.json