package com.example.shopy.datalayer.entity.itemPojo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "wishList")
data class Product (
	@PrimaryKey
	@SerializedName("id") val id : Long,
	@SerializedName("title") val title : String?,
	@SerializedName("body_html") val body_html : String?,
	@SerializedName("vendor") val vendor : String?,
	@SerializedName("product_type") val product_type : String?,
	@SerializedName("created_at") val created_at : String?,
	@SerializedName("handle") val handle : String?,
	@SerializedName("updated_at") val updated_at : String?,
	@SerializedName("published_at") val published_at : String?,
	@SerializedName("template_suffix") val template_suffix : String?,
	@SerializedName("status") val status : String?,
	@SerializedName("published_scope") val published_scope : String?,
	@SerializedName("tags") val tags : String?,
	@SerializedName("admin_graphql_api_id") val admin_graphql_api_id : String?,
	@SerializedName("variants") val variants : List<Variants>?,
	@SerializedName("options") var options : List<Options>?,
	@SerializedName("images") val images : List<Images>?,
	@SerializedName("image") val image : Image
)