package com.example.shopy.datalayer.onlineDataLayer.productDetailsService

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.entity.itemPojo.ProductItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProuductDetailsReposatory(val productDetailsDao : ProductDetailsDao) :
    ProuductDetailsDataSource {
    var prouductDetaild : MutableLiveData<ProductItem> = MutableLiveData()


    override fun getProuduct(id: Long){
        productDetailsDao.getOneProduct(id).enqueue(object : Callback<ProductItem?> {
            override fun onResponse(call: Call<ProductItem?>, response: Response<ProductItem?>) {
                Log.d("TAG","data here")
                prouductDetaild.value = response.body()
            }
            override fun onFailure(call: Call<ProductItem?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


}