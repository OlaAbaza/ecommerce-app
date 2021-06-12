package com.example.shopy.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.data.dataLayer.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(val repository: Repository, application: Application):AndroidViewModel(application) {
    fun clearRoom(){
   CoroutineScope(Dispatchers.IO).launch {
       repository.deleteAllFromCart()
       repository.deleteAllFromWish()

   }



    }
}