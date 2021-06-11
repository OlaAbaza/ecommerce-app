package com.example.shopy.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopy.dataLayer.Repository
import com.example.shopy.domainLayer.FilterData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler

class MainActivityViewModel(val repository: Repository, application: Application): AndroidViewModel(application) {

    fun getAllWishList()= repository.getAllWishList()

    fun getAllCartList() = repository.getAllCartList()
}