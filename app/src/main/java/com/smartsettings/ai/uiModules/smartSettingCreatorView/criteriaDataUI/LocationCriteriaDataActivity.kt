package com.smartsettings.ai.uiModules.smartSettingCreatorView.criteriaDataUI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.smartsettings.ai.R
import com.smartsettings.ai.utils.LocationUtils
import kotlinx.android.synthetic.main.activity_location_criteria.*

class LocationCriteriaDataActivity : AppCompatActivity() {

    companion object {

        const val LAT_STR = "lat"
        const val LON_STR = "lon"
        const val RADIUS_STR = "radius"

        fun open(activity: AppCompatActivity, reqCode: Int) {
            val intent = Intent(activity, LocationCriteriaDataActivity::class.java)
            activity.startActivityForResult(intent, reqCode)
        }
    }

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_criteria)

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            googleMap = it
            googleMap?.uiSettings?.isCompassEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        }

        confirmButton.setOnClickListener {
            val lat = googleMap?.cameraPosition?.target?.latitude ?: 0.0
            val lon = googleMap?.cameraPosition?.target?.longitude ?: 0.0
            val leftLat = googleMap?.projection?.visibleRegion?.farLeft?.latitude ?: 0.0
            val leftLon = googleMap?.projection?.visibleRegion?.farLeft?.longitude ?: 0.0

            val radius = LocationUtils.getDistanceInMetre(Pair(lat, lon), Pair(leftLat, leftLon))

            val intent = Intent()
            intent.putExtra(LAT_STR, lat)
            intent.putExtra(LON_STR, lon)
            intent.putExtra(RADIUS_STR, radius)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
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
}