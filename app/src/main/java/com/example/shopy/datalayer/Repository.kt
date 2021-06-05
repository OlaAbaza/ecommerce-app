package com.example.shopy.datalayer

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.ProductsList
import com.example.shopy.datalayer.network.DataSource
import com.example.shopy.datalayer.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(application: Application) {


    var remoteDataSource: DataSource
    var womanProductsList = MutableLiveData<ProductsList>()
    var kidsProductsList = MutableLiveData<ProductsList>()
    var menProductsList = MutableLiveData<ProductsList>()
    var onSaleProductsList = MutableLiveData<ProductsList>()
    var allProductsList = MutableLiveData<AllProducts>()
    var allDiscountCodeList = MutableLiveData<AllCodes>()


    init {
        remoteDataSource = DataSource(Network.apiService)
        Log.i("output", "repo")

    }



    fun getWomanProductsListFromApi() {
        Log.i("output", "getWomanProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

          remoteDataSource.getWomanProductsList().enqueue(object : Callback<ProductsList?> {

              override fun onResponse(call: Call<ProductsList?>, response: Response<ProductsList?>) {
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

    fun getKidProductsListFromApi() {
        Log.i("output", "getKidProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            remoteDataSource.getKidsProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(call: Call<ProductsList?>, response: Response<ProductsList?>) {
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


    fun getMenProductsListFromApi() {
        Log.i("output", "getMenProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            remoteDataSource.getMenProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(call: Call<ProductsList?>, response: Response<ProductsList?>) {
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



    fun getOnSaleProductsListFromApi() {
        Log.i("output", "getOnSaleProductsListFromApi ** repo")

        CoroutineScope(Dispatchers.IO).launch {

            remoteDataSource.getOnSaleProductsList().enqueue(object : Callback<ProductsList?> {

                override fun onResponse(call: Call<ProductsList?>, response: Response<ProductsList?>) {
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

    fun getAllProductsListFromApi() {

        CoroutineScope(Dispatchers.IO).launch {

            remoteDataSource.getAllProductsList().enqueue(object : Callback<AllProducts?> {

                override fun onResponse(call: Call<AllProducts?>, response: Response<AllProducts?>) {
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

    fun getAllDiscountCodeList() {

        CoroutineScope(Dispatchers.IO).launch {

            remoteDataSource.getAllDiscountCodeList().enqueue(object : Callback<AllCodes?> {

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

}





