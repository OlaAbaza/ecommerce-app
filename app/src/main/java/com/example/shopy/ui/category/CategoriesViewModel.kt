package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.datalayer.entity.custom_product.Product

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(val repository: Repository, application: Application):AndroidViewModel(application) {
    fun fetchCatProducts(colID:Long)= repository.fetchCatProducts(colID)
    fun fetchAllProducts()=repository.fetchAllProducts()

}