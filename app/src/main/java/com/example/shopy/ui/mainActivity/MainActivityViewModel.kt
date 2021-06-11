package com.example.shopy.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.dataLayer.Repository

class MainActivityViewModel(val repository: Repository, application: Application): AndroidViewModel(application) {

    fun getAllWishList()= repository.getAllWishList()

    fun getAllCartList() = repository.getAllCartList()
}