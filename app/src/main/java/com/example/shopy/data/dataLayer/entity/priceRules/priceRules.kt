package com.example.shopy.data.dataLayer.entity.priceRules


import com.google.gson.annotations.SerializedName

data class priceRules(
    @SerializedName("price_rules")
    val priceRules: List<PriceRule>?
)