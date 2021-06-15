package com.example.shopy.data.dataLayer.entity.priceRules


import com.google.gson.annotations.SerializedName

data class PrerequisiteToEntitlementQuantityRatio(
    @SerializedName("entitled_quantity")
    val entitledQuantity: Any?,
    @SerializedName("prerequisite_quantity")
    val prerequisiteQuantity: Any?
)