package com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.smartsettings.ai.R
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.utils.LocationUtils
import kotlinx.android.synthetic.main.fragment_location_input.*

class LocationInputFragment(val locationData: LocationData?) : Fragment(), SmartSettingInputView<LocationData> {

    private var googleMap: GoogleMap? = null

    override fun getInput(): LocationData {
        val lat = googleMap?.cameraPosition?.target?.latitude ?: 0.0
        val lon = googleMap?.cameraPosition?.target?.longitude ?: 0.0
        val leftLat = googleMap?.projection?.visibleRegion?.farLeft?.latitude ?: 0.0
        val leftLon = googleMap?.projection?.visibleRegion?.farLeft?.longitude ?: 0.0

        val radius = LocationUtils.getDistanceInMetre(Pair(lat, lon), Pair(leftLat, leftLon))

        return LocationData(lat, lon, radius)
    }

    override fun validate(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            googleMap = it
            it.uiSettings.isMapToolbarEnabled = true
            it.uiSettings.isScrollGesturesEnabled = true
            it.uiSettings.isZoomControlsEnabled = true
            it.uiSettings.isZoomGesturesEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}