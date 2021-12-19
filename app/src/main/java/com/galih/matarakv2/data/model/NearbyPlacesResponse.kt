package com.galih.matarakv2.data.model

data class NearbyPlacesResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Places>,
    val status: String
    )

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Places(
    val geometry: Geometry,
    val name: String,
    val place_id: String,
)