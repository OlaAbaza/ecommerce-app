package com.example.shopy.datalayer.entity.itemPojo
import com.google.gson.annotations.SerializedName


data class ProductItem (
	@SerializedName("product") val product : Product
)