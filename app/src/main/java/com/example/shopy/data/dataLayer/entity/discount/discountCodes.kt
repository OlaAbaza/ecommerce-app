package com.example.shopy.data.dataLayer.entity.discount


import com.google.gson.annotations.SerializedName

data class discountCodes(
    @SerializedName("discount_codes")
    val discountCodes: List<DiscountCode>?
)