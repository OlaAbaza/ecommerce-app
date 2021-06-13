package com.example.shopy.data.dataLayer.remoteDataLayer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.priceRules.priceRules
import com.example.shopy.data.dataLayer.itemPojo.Delete
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import com.example.shopy.datalayer.network.Network
import com.example.shopy.models.*
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RemoteDataSourceImpl : RemoteDataSource {

    var womanProducts = MutableLiveData<ProductsList>()
    var kidsProducts = MutableLiveData<ProductsList>()
    var menProducts = MutableLiveData<ProductsList>()
    var onSaleProducts = MutableLiveData<ProductsList>()
    var allProductsListt = MutableLiveData<AllProducts>()


    var allDiscountCode = MutableLiveData<AllCodes>()
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()
    var deleteOrder : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var createOrder: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var catProducts = MutableLiveData<List<Product>>()
    var allProducts = MutableLiveData<List<com.example.shopy.datalayer.entity.itemPojo.Product>>()


    override suspend fun fetchCustomersData(): List<Customer>? {
        val response = Network.apiService.getCustomers()
        try {
            if (response.isSuccessful) {
                Timber.i("olaa fetchCustomersData  "+response.body()?.customers)
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
                         //   .getJSONArray("email").get(0).toString()
                    Timber.i("olaa This email " + jObjError)
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

    override suspend fun getCustomer(id: String): CustomerX? {
        val response = Network.apiService.getCustomer(id)
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

    override suspend fun updateCustomer(id: String, customer: CustomerProfile): CustomerX? {
        val response = Network.apiService.updateCustomer(id,customer)
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

    override fun createOrder(order: Orders) {
        CoroutineScope(Dispatchers.IO).launch {

            val response = Network.apiService.createOrder(order)
            try {
                if (response.isSuccessful) {
                    Timber.i("This  " + "isSuccessful")
                    createOrder.postValue(true)
                    // return response.body()
                } else {
                    createOrder.postValue(false)
                    val jObjError =
                        JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                    Timber.i("This  " + jObjError)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
            // return null
        }

    }

    override fun getCreateOrderResponse(): MutableLiveData<Boolean> {
        return createOrder
    }

    override fun getWomanProductsList(): MutableLiveData<ProductsList> {
        Log.i("output", "getWomanProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getWomanProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        womanProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return womanProducts
    }

    override  fun getMenProductsList() : MutableLiveData<ProductsList> {
        Log.i("output", "getMenProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getMenProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        menProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return menProducts
    }

    override  fun getKidsProductsList() : MutableLiveData<ProductsList>{
        Log.i("output", "getKidProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getKidsProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        kidsProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return kidsProducts
    }

    override  fun getOnSaleProductsList(): MutableLiveData<ProductsList> {
        Log.i("output", "getOnSaleProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getOnSaleProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(
                    call: Call<ProductsList?>,
                    response: Response<ProductsList?>
                ) {
                    if (response.isSuccessful) {
                        onSaleProducts.postValue(response.body())
                        // Log.i("output", response.body().toString())

                    }
                }

                override fun onFailure(call: Call<ProductsList?>, t: Throwable) {
                    Log.i("output", t.message.toString())
                    t.printStackTrace()

                }
            })
        }
        return onSaleProducts
    }

    override  fun getAllProductsList() : MutableLiveData<AllProducts>{

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllProductsList().enqueue(object : Callback<AllProducts?> {

                override fun onResponse(
                    call: Call<AllProducts?>,
                    response: Response<AllProducts?>
                ) {
                    if (response.isSuccessful) {
                        allProductsListt.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<AllProducts?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
        return allProductsListt
    }

    override  fun getAllDiscountCodeList() : MutableLiveData<AllCodes> {

        CoroutineScope(Dispatchers.IO).launch {

            Network.apiService.getAllDiscountCodeList().enqueue(object : Callback<AllCodes?> {

                override fun onResponse(call: Call<AllCodes?>, response: Response<AllCodes?>) {
                    if (response.isSuccessful) {
                        allDiscountCode.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<AllCodes?>, t: Throwable) {
                    t.printStackTrace()


                }
            })
        }
        return allDiscountCode
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


    override fun getAllOrders(): Observable<GetOrders> {
        return Network.apiService.getAllOrders()

    }

    override fun deleteOrder(order_id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            Network.apiService.deleteOrder(order_id).enqueue(object : Callback<Delete?> {
                override fun onResponse(call: Call<Delete?>, response: Response<Delete?>) {
//                    if (response.isSuccessful) {
                        deleteOrder.postValue(true)
//                    }
                }

                override fun onFailure(call: Call<Delete?>, t: Throwable) {
                }
            })
        }
    }
    override fun observeDeleteOrder():MutableLiveData<Boolean>{
        return deleteOrder
    }

}
