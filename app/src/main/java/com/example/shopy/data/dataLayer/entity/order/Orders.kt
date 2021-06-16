package com.example.shopy.models


import com.example.shopy.datalayer.entity.ads_discount_codes.DiscountCode
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Orders(
    val order: Order?
)

data class DiscountCodes(
    val amount: String?,
    val code: String?
): Serializable

data class Order(
    val customer: CustomerOrder?,
    @SerializedName("financial_status")
    val financialStatus: String?,
    @SerializedName("line_items")
    val lineItems: List<LineItem>?,
    val note: String?,
    @SerializedName("discount_codes")
    val discountCodes: List<DiscountCodes>?
)

data class LineItem(
    val quantity: Int?,
    @SerializedName("variant_id")
    val variantId: Long?
)

data class CustomerOrder(
    val id: Long?
)
