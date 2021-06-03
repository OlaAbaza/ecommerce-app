package com.example.shopy

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerX
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

    override suspend fun createCustomers(customer: CustomerX): String {
        val response = ServiceBuilder.getApi().createCustomer(customer)
        var result = ""
        try {
            response?.let {
                if (response.isSuccessful) {
                    result = "true"
                } else {
                    val jObjError =
                        JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                            .getJSONArray("email").get(0).toString()
                    result = "This email " + jObjError
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
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

