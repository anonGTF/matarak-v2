package com.galih.matarakv2.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.galih.matarakv2.R
import com.galih.matarakv2.base.BaseFragment
import com.galih.matarakv2.data.model.Places
import com.galih.matarakv2.databinding.FragmentNearestHospitalBinding
import com.github.florent37.runtimepermission.PermissionResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class NearestHospitalFragment : BaseFragment<FragmentNearestHospitalBinding>(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNearestHospitalBinding
        = FragmentNearestHospitalBinding::inflate
    private lateinit var mMaps: GoogleMap
    private val viewModel: MapsViewModel by activityViewModels()
    private val fusedLocationClient: FusedLocationProviderClient
        by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }

    override fun setup() {
        val mFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mFragment.getMapAsync(this)
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        checkLocationPermissions {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let { currentLocation ->
                    val mLoc = LatLng(currentLocation.latitude, currentLocation.longitude)
                    mMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(mLoc, 15f))

                    getNearbyHospital(currentLocation.latitude.toString(), currentLocation.longitude.toString())
                }
            }
        }
    }

    private fun getNearbyHospital(lat: String, long: String) {
        viewModel.findNearbyHospital(lat, long).observe(viewLifecycleOwner, setupPlacesObserver())
    }

    private fun setupPlacesObserver() = setObserver<List<Places>?>(
        onSuccess = { response ->
            response.data?.forEach {
                val lat = it.geometry.location.lat
                val lng = it.geometry.location.lng

                val marker = mMaps.addMarker(MarkerOptions().position(LatLng(lat,lng)).title(it.name))
                marker?.tag = it.place_id
                mMaps.setOnMarkerClickListener(this)
            }
        },
        onError = { response ->
            showToast(response.message.toString())
        }
    )

    private fun checkLocationPermissions(onAccepted: (PermissionResult) -> Unit) {
        askPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            onAccepted = { onAccepted.invoke(it) })
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(gMap: GoogleMap) {
        mMaps = gMap
        checkLocationPermissions {
            mMaps.isMyLocationEnabled = true
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val dir = marker.title
        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(BASE_DIRECTION_URL + URLEncoder.encode(dir, "UTF-8")))
        startActivity(intent)
        return true
    }

    companion object {
        const val BASE_DIRECTION_URL = "https://www.google.com/maps/dir/?api=1&destination="
    }
}