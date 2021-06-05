package com.example.shopy.datalayer.network

class DataSource(private val apiInterface : NetworkService) {
     fun getWomanProductsList() = apiInterface.getWomanProductsList()
     fun getKidsProductsList() = apiInterface.getKidsProductsList()
     fun getMenProductsList() = apiInterface.getMenProductsList()
     fun getOnSaleProductsList() = apiInterface.getOnSaleProductsList()
     fun getAllProductsList() = apiInterface.getAllProductsList()
     fun getAllDiscountCodeList() = apiInterface.getAllDiscountCodeList()

}

