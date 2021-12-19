package com.galih.matarakv2.data.remote

import com.galih.matarakv2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    fun createApi(): MapsApi {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_MAPS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()

        return retrofit.create(MapsApi::class.java)
    }
}