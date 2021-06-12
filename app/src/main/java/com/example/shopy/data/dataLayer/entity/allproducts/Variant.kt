package com.example.shopy.datalayer.entity.allproducts


import com.example.shopy.datalayer.entity.allproducts.PresentmentPrice
import com.google.gson.annotations.SerializedName

data class Variant(
        @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,
        val barcode: String,
        @SerializedName("compare_at_price")
    val compareAtPrice: Any,
        @SerializedName("created_at")
    val createdAt: String,
        @SerializedName("fulfillment_service")
    val fulfillmentService: String,
        val grams: Double,
        val id: Double,
        @SerializedName("image_id")
    val imageId: Double,
        @SerializedName("inventory_item_id")
    val inventoryItemId: Double,
        @SerializedName("inventory_management")
    val inventoryManagement: String,
        @SerializedName("inventory_policy")
    val inventoryPolicy: String,
        @SerializedName("inventory_quantity")
    val inventoryQuantity: Double,
        @SerializedName("old_inventory_quantity")
    val oldInventoryQuantity: Double,
        val option1: String,
        val option2: Any,
        val option3: Any,
        val position: Double,
        @SerializedName("presentment_prices")
    val presentmentPrices: List<PresentmentPrice>,
        val price: String,
        @SerializedName("product_id")
    val productId: Double,
        @SerializedName("requires_shipping")
    val requiresShipping: Boolean,
        val sku: String,
        val taxable: Boolean,
        val title: String,
        @SerializedName("updated_at")
    val updatedAt: String,
        val weight: Double,
        @SerializedName("weight_unit")
    val weightUnit: String
)