package com.example.shopy.datalayer.onlineDataLayer.productDetailsService

import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductItem

interface ProuductDetailsDataSource {
    fun getProuduct(id : Long)
}