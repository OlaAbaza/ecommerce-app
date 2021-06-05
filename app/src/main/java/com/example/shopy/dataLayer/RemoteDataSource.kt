package com.example.shopy.dataLayer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.shopy.models.*
import org.json.JSONObject
import timber.log.Timber


class RemoteDataSource : RemoteInterface {

    override suspend fun fetchCustomersData(): List<Customer>? {
        val response = ServiceBuilder.getApi().getCustomers()
        try {
            if (response.isSuccessful) {
                return response.body()?.customers
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun createCustomers(customer: CustomerX): CustomerX? {
        val response = ServiceBuilder.getApi().createCustomer(customer)
        try {
            response?.let {
                if (response.isSuccessful) {
                    return response.body()
                } else {
                    val jObjError =
                        JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                            .getJSONArray("email").get(0).toString()
                   Timber.i("This email " + jObjError)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun createCustomerAddress(
        id: String,
        customerAddress: CreateAddress
    ): CreateAddressX? {
        val response = ServiceBuilder.getApi().createCustomerAddress(id, customerAddress)
        try {
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getCustomerAddresses(id: String): List<Addresse>? {
        val response = ServiceBuilder.getApi().getCustomerAddresses(id)
        try {
            if (response.isSuccessful) {
                return response.body()?.addresses
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun updateCustomerAddresses(
        id: String,
        addressID: String,
        customerAddress: UpdateAddresse
    ): CreateAddressX? {
        val response =
            ServiceBuilder.getApi().updateCustomerAddresses(id, addressID, customerAddress)
        try {
            if (response.isSuccessful) {
                return response.body()
            } else {
                val jObjError =
                    JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                Timber.i("This  " + jObjError)

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun delCustomerAddresses(id: String, addressID: String) {
        val response = ServiceBuilder.getApi().delCustomerAddresses(id, addressID)
        try {
            if (response.isSuccessful) {
                Timber.i("This  " + "isSuccessful")
            } else {
                val jObjError =
                    JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                Timber.i("This  " + jObjError)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override suspend fun setDafaultCustomerAddress(id: String, addressID: String): CreateAddressX? {
        val response = ServiceBuilder.getApi().setDafaultCustomerAddress(id, addressID)
        try {
            if (response.isSuccessful) {
                Timber.i("This  " + "isSuccessful")
                return response.body()
            } else {
                val jObjError =
                    JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                Timber.i("This  " + jObjError)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}

