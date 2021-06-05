package com.example.shopy.dataLayer

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val BASE_URL =
    "https://ce751b18c7156bf720ea405ad19614f4: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@itiana.myshopify.com/admin/api/2021-04/"

object ServiceBuilder {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(buildAuthClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApi() = retrofit.create(RestApi::class.java)

}

private fun buildAuthClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Shopify-Access-Token", "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")
            .build()
        chain.proceed(newRequest)
    }

    httpClient.connectTimeout(60, TimeUnit.SECONDS)
    httpClient.readTimeout(60, TimeUnit.SECONDS)
    httpClient.writeTimeout(30, TimeUnit.SECONDS);
    return httpClient.build()
}
