package com.example.shopy.dataLayer

import com.example.shopy.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RestApi {

    @GET("customers.json")
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

    @PUT(" customers/{customer_id}/addresses/{address_id}.json")
    suspend fun updateCustomerAddresses(
        @Path("customer_id") id: String,
        @Path("address_id") addressID: String,
        @Body customerAddress: UpdateAddresse
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

}