package com.example.shopy.datalayer.localdatabase.room


import androidx.room.TypeConverter
import com.example.shopy.datalayer.entity.itemPojo.Image
import com.example.shopy.datalayer.entity.itemPojo.Images
import com.example.shopy.datalayer.entity.itemPojo.Options
import com.example.shopy.datalayer.entity.itemPojo.Variants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun fromWetherToGesonx(list: List<Variants>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToWeatherx(gson: String): List<Variants> {
        val type = object : TypeToken<List<Variants>>() {}.type
        return Gson().fromJson(gson, type)
    }


    @TypeConverter
    fun fromOptionsToGesonx(list: List<Options>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonTOptions(gson: String): List<Options> {
        val type = object : TypeToken<List<Variants>>() {}.type
        return Gson().fromJson(gson, type)
    }


    @TypeConverter
    fun fromImagesToGesonx(list: List<Images>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonTOImages(gson: String): List<Images> {
        val type = object : TypeToken<List<Variants>>() {}.type
        return Gson().fromJson(gson, type)
    }

    @TypeConverter
    fun fromImageToGesonx(list: Image): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonTOImage(gson: String): Image {
        val type = object : TypeToken<List<Variants>>() {}.type
        return Gson().fromJson(gson, type)
    }


}