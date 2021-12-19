package com.galih.matarakv2.ui.maps

import com.galih.matarakv2.base.BaseViewModel
import com.galih.matarakv2.data.firebase.FirebaseUtils
import com.galih.matarakv2.data.remote.MapsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val firebase: FirebaseUtils,
    private val api: MapsApi
) : BaseViewModel() {

    fun findNearbyHospital(lat: String, long: String, radius: Int = 10000, keyword: String = "Rumah sakit") = callApiReturnLiveData(
        apiCall = {
            val response = api.nearbyPlace("$lat,$long", radius, keyword)
            response.body()?.results
        }
    )
}