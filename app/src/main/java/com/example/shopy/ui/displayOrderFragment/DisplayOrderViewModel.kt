package com.example.shopy.ui.displayOrderFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopy.dataLayer.Repository
import com.example.shopy.datalayer.network.Network
import com.example.shopy.models.OrderResponse
import io.reactivex.Observable

class DisplayOrderViewModel(val repository: Repository, application: Application): AndroidViewModel(application) {
     fun getAllOrders(): Observable<List<OrderResponse>> {
        return repository.getAllOrders()
    }
}