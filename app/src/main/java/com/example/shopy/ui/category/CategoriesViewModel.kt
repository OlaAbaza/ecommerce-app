package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.Repository

class CategoriesViewModel(val repository: Repository, application: Application):AndroidViewModel(application) {
    fun fetchCatProducts(colID:Long)= repository.fetchCatProducts(colID)
    fun fetchAllProducts()=repository.fetchAllProducts()

}