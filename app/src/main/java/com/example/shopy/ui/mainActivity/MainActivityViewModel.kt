package com.example.shopy.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl

class MainActivityViewModel(val repositoryImpl: IRepository, application: Application): AndroidViewModel(application) {

    fun getAllWishList()= repositoryImpl.getAllWishList()

    fun getAllCartList() = repositoryImpl.getAllCartList()
}