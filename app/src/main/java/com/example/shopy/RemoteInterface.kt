package com.example.shopy


import android.content.Context
import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX


interface RemoteInterface {
    suspend fun fetchCustomersData(): List<Customer>?
    suspend fun createCustomers(customer: CustomerX): String
    fun isOnline(context: Context): Boolean
}