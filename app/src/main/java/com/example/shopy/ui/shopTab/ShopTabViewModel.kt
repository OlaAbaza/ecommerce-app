package com.example.shopy.ui.shopTab

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.datalayer.Repository
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.allproducts.AllProducts
import com.example.shopy.datalayer.entity.allproducts.allProduct
import com.example.shopy.datalayer.entity.custom_product.ProductsList

class ShopTabViewModel(application: Application) : AndroidViewModel(application) {

    var repository: Repository
    init {
        repository = Repository(application)
        Log.i("output","viewModel")
    }
    val error = MutableLiveData<Boolean>()
    val navigation = MutableLiveData<Int>()
    var shopSearchListLiveData = MutableLiveData<ArrayList<allProduct>>()



    fun fetchWomanProductsList() : MutableLiveData<ProductsList> {
        repository.getWomanProductsListFromApi()
       return repository.womanProductsList
    }

    fun fetchMenProductsList() : MutableLiveData<ProductsList> {
        repository.getMenProductsListFromApi()
        return repository.menProductsList
    }

    fun fetchOnSaleProductsList() : MutableLiveData<ProductsList> {
        repository.getOnSaleProductsListFromApi()
        return repository.onSaleProductsList
    }

    fun fetchKidsProductsList() : MutableLiveData<ProductsList> {
        repository.getKidProductsListFromApi()
        return repository.kidsProductsList
    }

    fun fetchallProductsList() : MutableLiveData<AllProducts> {
        repository.getAllProductsListFromApi()
        return repository.allProductsList
    }

    fun fetchallDiscountCodeList() : MutableLiveData<AllCodes> {
        repository.getAllDiscountCodeList()
        return repository.allDiscountCodeList
    }




    fun onClick(id: Int) {
        if (id == 0) {
            navigation.setValue(id)
        } else navigation.setValue(id)
    }


}