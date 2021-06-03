package com.example.shopy.datalayer.onlineDataLayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProuductDetailsReposatory(val productDetailsDao : ProductDetailsDao) : ProuductDetailsDataSource {
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()


    override fun getProuduct(id: Long) : LiveData<ProductItem> {
        Log.d("TAG","data here")

        productDetailsDao.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(call: Call<ProductItem?>, response: Response<ProductItem?>) {
                Log.d("TAG","data here ${response.body().toString()}")
                if (response.isSuccessful){
                    prouductDetaild.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return prouductDetaild
    }
}