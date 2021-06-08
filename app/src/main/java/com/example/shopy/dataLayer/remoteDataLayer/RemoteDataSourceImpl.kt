package com.example.shopy.dataLayer.remoteDataLayer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.datalayer.network.Network
import com.example.shopy.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RemoteDataSourceImpl : RemoteDataSource {

    var womanProductsList = MutableLiveData<ProductsList>()
    var kidsProductsList = MutableLiveData<ProductsList>()
    var menProductsList = MutableLiveData<ProductsList>()
    var onSaleProductsList = MutableLiveData<ProductsList>()
    var allProductsList = MutableLiveData<AllProducts>()
    var allDiscountCodeList = MutableLiveData<AllCodes>()
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()

    var catProducts = MutableLiveData<List<Product>>()
    var allProducts = MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>()



    override suspend fun fetchCustomersData(): List<Customer>? {
        val response = Network.apiService.getCustomers()
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
        val response = Network.apiService.createCustomer(customer)
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
        val response = Network.apiService.createCustomerAddress(id, customerAddress)
        try {
            if (response.isSuccessful) {
                return response.body()
            }
            else{
                val jObjError =
                    JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                Timber.i("This  " + jObjError)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getCustomerAddresses(id: String): List<Addresse>? {
        val response = Network.apiService.getCustomerAddresses(id)
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
            Network.apiService.updateCustomerAddresses(id, addressID, customerAddress)
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

    override suspend fun getCustomerAddress(
        id: String,
        addressID: String
    ): CreateAddressX? {
        val response = Network.apiService.getCustomerAddress(id,addressID)
        try {
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    override suspend fun delCustomerAddresses(id: String, addressID: String) {
        val response = Network.apiService.delCustomerAddresses(id, addressID)
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
        val response = Network.apiService.setDafaultCustomerAddress(id, addressID)
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

    override suspend fun createOrder(order: Orders): OrderResponse? {
        val response = Network.apiService.createOrder(order)
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

    override  fun getWomanProductsList() {
        Log.i("output", "getWomanProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getWomanProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        womanProductsList.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
    }

    override  fun getMenProductsList() {
        Log.i("output", "getMenProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getMenProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        menProductsList.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
    }

    override  fun getKidsProductsList() {
        Log.i("output", "getKidProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getKidsProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        kidsProductsList.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
    }


    override  fun getOnSaleProductsList() {
        Log.i("output", "getOnSaleProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getOnSaleProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        onSaleProductsList.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
    }

    override  fun getAllProductsList() {

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllProductsList().enqueue(object : Callback<AllProducts?> {

                override fun onResponse(
                    call: Call<AllProducts?>,
                    response: Response<AllProducts?>
                ) {
                    if (response.isSuccessful) {
                        allProductsList.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<AllProducts?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
    }


    override  fun getAllDiscountCodeList() {

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllDiscountCodeList().enqueue(object : Callback<AllCodes?> {

                override fun onResponse(call: Call<AllCodes?>, response: Response<AllCodes?>) {
                    if (response.isSuccessful) {
                        allDiscountCodeList.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<AllCodes?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
    }



        override fun getProuduct(id: Long){
            Network.apiService.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
                override fun onResponse(call: Call<ProductItem?>, response: Response<ProductItem?>) {
                    Log.d("TAG","data here")
                    prouductDetaild.value = response.body()
                }
                override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
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

    override fun fetchCatProducts(colID: Long): MutableLiveData<List<Product>> {
        var categoryRetrofitApi = Network.apiService
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoryRetrofitApi.getProducts(colID)
            if (response.isSuccessful) {
                catProducts.postValue(response.body()!!.products)
            } else {
                //error code
            }
        }
        return catProducts
    }

    override fun fetchAllProducts(): MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>> {
        var categoryRetrofitApi = Network.apiService
        CoroutineScope(Dispatchers.IO).launch {
            val response=categoryRetrofitApi.getAllProducts()
            if (response.isSuccessful){
                allProducts.postValue(response.body()!!)

            }
        }
        return allProducts
    }




}