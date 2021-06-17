package com.example.shopy.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.IRepository
import com.example.shopy.data.dataLayer.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(val repositoryImpl: IRepository, application: Application):AndroidViewModel(application) {
    fun clearRoom(){
   CoroutineScope(Dispatchers.IO).launch {
       repositoryImpl.deleteAllFromCart()
       repositoryImpl.deleteAllFromWish()

   }



    }
}