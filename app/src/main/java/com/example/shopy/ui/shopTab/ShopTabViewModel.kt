package com.example.shopy.ui.shopTab

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList

class ShopTabViewModel(val repository: Repository, application: Application) : AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()


    fun fetchAllProducts()=repository.fetchAllProducts()

    fun fetchWomanProductsList() : MutableLiveData<ProductsList> {
        return repository.getWomanProductsList()
    }

    fun fetchMenProductsList() : MutableLiveData<ProductsList> {
        return repository.getMenProductsList()
    }

    fun fetchOnSaleProductsList() : MutableLiveData<ProductsList> {
        return repository.getOnSaleProductsList()
    }

    fun fetchKidsProductsList() : MutableLiveData<ProductsList> {
        return repository.getKidsProductsList()
    }

    fun fetchallProductsList() : MutableLiveData<AllProducts> {
        return repository.getAllProductsList()
    }

    fun fetchallDiscountCodeList() : MutableLiveData<AllCodes> {
        return repository.getAllDiscountCodeList()
    }

}