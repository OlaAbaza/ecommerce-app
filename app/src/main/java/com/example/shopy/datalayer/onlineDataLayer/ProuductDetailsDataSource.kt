package com.example.shopy.datalayer.onlineDataLayer

import androidx.lifecycle.LiveData
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductItem

interface ProuductDetailsDataSource {
//    fun getProuduct(id : Long) : LiveData<Product>
    fun getProuduct(id : Long) : LiveData<ProductItem>

}