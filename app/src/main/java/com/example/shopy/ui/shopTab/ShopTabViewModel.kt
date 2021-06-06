package com.example.shopy.ui.shopTab

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList

class ShopTabViewModel(application: Application) : AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()

    var remoteDataSource: RemoteDataSourceImpl
    init {
        remoteDataSource = RemoteDataSourceImpl()
        Log.i("output","viewModel")
    }
    val error = MutableLiveData<Boolean>()
    val navigation = MutableLiveData<Int>()
    var shopSearchListLiveData = MutableLiveData<ArrayList<allProduct>>()



    fun fetchWomanProductsList() : MutableLiveData<ProductsList> {
        remoteDataSource.getWomanProductsList()
       return remoteDataSource.womanProductsList
    }

    fun fetchMenProductsList() : MutableLiveData<ProductsList> {
        remoteDataSource.getMenProductsList()
        return remoteDataSource.menProductsList
    }

    fun fetchOnSaleProductsList() : MutableLiveData<ProductsList> {
        remoteDataSource.getOnSaleProductsList()
        return remoteDataSource.onSaleProductsList
    }

    fun fetchKidsProductsList() : MutableLiveData<ProductsList> {
        remoteDataSource.getKidsProductsList()
        return remoteDataSource.kidsProductsList
    }

    fun fetchallProductsList() : MutableLiveData<AllProducts> {
        remoteDataSource.getAllProductsList()
        return remoteDataSource.allProductsList
    }

    fun fetchallDiscountCodeList() : MutableLiveData<AllCodes> {
        remoteDataSource.getAllDiscountCodeList()
        return remoteDataSource.allDiscountCodeList
    }




    fun onClick(id: Int) {
        if (id == 0) {
            navigation.setValue(id)
        } else navigation.setValue(id)
    }


}