package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl

class CategoriesViewModel(val repositoryImpl: IRepository, application: Application):AndroidViewModel(application) {
    fun fetchCatProducts(colID:Long)= repositoryImpl.fetchCatProducts(colID)
    fun fetchAllProducts()=repositoryImpl.fetchAllProducts()

}