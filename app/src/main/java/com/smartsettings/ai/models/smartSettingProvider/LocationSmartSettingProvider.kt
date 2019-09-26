package com.smartsettings.ai.models.smartSettingProvider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.smartsettings.ai.R
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.models.smartSettings.SmartSetting
import kotlinx.android.synthetic.main.item_loc_smart_setting_provider.view.*

class LocationSmartSettingProvider : SmartSettingProvider {

    var lat: Double = 0.0

    var lon: Double = 0.0

    var volume: Int = 100

    var radiusInMetre: Int = 500

    override fun getView(context: Context, getSmartSetting: (SmartSetting) -> Unit): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loc_smart_setting_provider, null)

        view.addBtn.setOnClickListener {

            lat = view.lattitudeText.text.toString().toDouble()
            lon = view.longitudeText.text.toString().toDouble()
            volume = view.volumeText.text.toString().toInt()

            getSmartSetting(LocationBasedVolumeSetting(lat, lon, radiusInMetre, volume))
        }

        return view
    }
}