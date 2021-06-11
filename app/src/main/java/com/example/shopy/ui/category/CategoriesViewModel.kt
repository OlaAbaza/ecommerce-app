package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CategoriesViewModel(val repository: Repository, application: Application):AndroidViewModel(application) {
    fun fetchCatProducts(colID:Long)= repository.fetchCatProducts(colID)
    fun fetchAllProducts()=repository.fetchAllProducts()

}