package com.example.shopy.ui.shopTab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.entity.custom_product.ProductsList

class ShopTabViewModel(val repositoryImpl: IRepository, application: Application) : AndroidViewModel(application) {

    var intentTOProductDetails : MutableLiveData<Product> = MutableLiveData()


    fun fetchAllProducts()=repositoryImpl.fetchAllProducts()

    fun fetchWomanProductsList() : MutableLiveData<ProductsList> {
        return repositoryImpl.getWomanProductsList()
    }

    fun fetchMenProductsList() : MutableLiveData<ProductsList> {
        return repositoryImpl.getMenProductsList()
    }

    fun fetchOnSaleProductsList() : MutableLiveData<ProductsList> {
        return repositoryImpl.getOnSaleProductsList()
    }

    fun fetchKidsProductsList() : MutableLiveData<ProductsList> {
        return repositoryImpl.getKidsProductsList()
    }

    fun fetchallProductsList() : MutableLiveData<AllProducts> {
        return repositoryImpl.getAllProductsList()
    }

    fun fetchallDiscountCodeList() : MutableLiveData<AllCodes> {
        return repositoryImpl.getAllDiscountCodeList()
    }

}