package com.example.shopy.datalayer.entity.custom_product


import com.google.gson.annotations.SerializedName

data class Option(
    val id: Double,
    val name: String,
    val position: Double,
    @SerializedName("product_id")
    val productId: Double
)