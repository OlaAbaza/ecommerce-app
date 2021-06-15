package com.example.shopy.data.dataLayer.entity.priceRules


import com.google.gson.annotations.SerializedName

data class PrerequisiteToEntitlementPurchase(
    @SerializedName("prerequisite_amount")
    val prerequisiteAmount: Any?
)