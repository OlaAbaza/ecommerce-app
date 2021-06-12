package com.example.shopy.datalayer.entity.allproducts


import com.google.gson.annotations.SerializedName

data class allProduct(
        @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,
        @SerializedName("body_html")
    val bodyHtml: String,
        @SerializedName("created_at")
    val createdAt: String,
        val handle: String,
        val id: Double,
        val image: Image,
        val images: List<Image>,
        val options: List<Option>,
        @SerializedName("product_type")
    val productType: String,
        @SerializedName("published_at")
    val publishedAt: String,
        @SerializedName("published_scope")
    val publishedScope: String,
        val tags: String,
        @SerializedName("template_suffix")
    val templateSuffix: Any,
        val title: String,
        @SerializedName("updated_at")
    val updatedAt: String,
        val variants: List<Variant>,
        val vendor: String
)