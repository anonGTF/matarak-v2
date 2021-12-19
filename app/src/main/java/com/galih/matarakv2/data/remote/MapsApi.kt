package com.galih.matarakv2.data.remote

import com.galih.matarakv2.BuildConfig
import com.galih.matarakv2.data.model.NearbyPlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {
    @GET("nearbysearch/json")
    suspend fun nearbyPlace(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("keyword") keyword: String,
        @Query("key") apiKey: String = BuildConfig.MAPS_API_KEY
    ) : Response<NearbyPlacesResponse>
}