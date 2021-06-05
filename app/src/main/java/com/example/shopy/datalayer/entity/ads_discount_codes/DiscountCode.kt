package com.example.shopy.datalayer.entity.ads_discount_codes


import com.google.gson.annotations.SerializedName

data class DiscountCode(
    val code: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Long,
    @SerializedName("price_rule_id")
    val priceRuleId: Long,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("usage_count")
    val usageCount: Int
)