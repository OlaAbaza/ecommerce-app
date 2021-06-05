package com.example.shopy.datalayer.onlineDataLayer

import com.example.shopy.datalayer.onlineDataLayer.productDetailsService.ProductDetailsDao
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object NetWorking {

    private fun buildAuthClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Shopify-Access-Token","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")
                .build()
            chain.proceed(newRequest)
        }

        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        return httpClient.build()
    }

    //val gson = GsonBuilder().setLenient().create()

    private fun getProudctDetailsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(buildAuthClient())
            .baseUrl(StaticUrls.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    val productDetailsDao: ProductDetailsDao = getProudctDetailsRetrofit().create(ProductDetailsDao::class.java)
}
