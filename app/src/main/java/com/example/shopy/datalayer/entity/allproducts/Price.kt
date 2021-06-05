package com.example.shopy.datalayer.entity.allproducts


import com.google.gson.annotations.SerializedName

data class Price(
    val amount: String,
    @SerializedName("currency_code")
    val currencyCode: String
)