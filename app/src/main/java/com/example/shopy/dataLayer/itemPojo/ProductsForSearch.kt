package com.example.shopy.datalayer.itemPojo

import com.example.shopy.datalayer.entity.itemPojo.Product
import com.google.gson.annotations.SerializedName

data class ProductsForSearch (
    @SerializedName("products") var products : List<Product>
    )