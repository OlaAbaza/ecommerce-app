package com.example.shopy.datalayer.entity.ads_discount_codes


import com.google.gson.annotations.SerializedName

data class AllCodes(
    @SerializedName("discount_codes")
    val discountCodes: List<DiscountCode>
)