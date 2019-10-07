package com.smartsettings.ai.uiModules.smartSettingProvider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.smartsettings.ai.R
import com.smartsettings.ai.core.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.data.criteriaData.LocationData
import kotlinx.android.synthetic.main.item_loc_smart_setting_provider.view.*

class LocationSmartSettingProvider : SmartSettingProvider {

    private var lat: Double = 0.0

    private var lon: Double = 0.0

    private var volume: Int = 100

    private var radiusInMetre: Int = 500

    override fun getView(context: Context, getSmartSetting: (SmartSetting<out Any>) -> Unit): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loc_smart_setting_provider, null)

        view.addBtn.setOnClickListener {

            lat = view.lattitudeText.text.toString().toDouble()
            lon = view.longitudeText.text.toString().toDouble()
            volume = view.volumeText.text.toString().toInt()

            getSmartSetting(
                LocationBasedVolumeSetting(
                    "Location",
                    LocationData(lat, lon, radiusInMetre)
                )
            )
        }

        return view
    }
}