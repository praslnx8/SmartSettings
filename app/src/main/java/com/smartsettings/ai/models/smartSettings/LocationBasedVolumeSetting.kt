package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Switch
import com.smartsettings.ai.R
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.actionData.VolumeActionData
import com.smartsettings.ai.models.contextData.LocationContext
import com.smartsettings.ai.models.contextListeners.ContextListener
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.models.criteriaData.LocationData
import com.smartsettings.ai.models.serializables.SerializableData
import com.smartsettings.ai.utils.LocationUtils
import javax.inject.Inject

class LocationBasedVolumeSetting(locationData: LocationData, volumeActionData: VolumeActionData) :
    SmartSetting<LocationContext, LocationData, VolumeActionData>(
        SerializableData(locationData),
        SerializableData(volumeActionData)
    ) {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var currentLocationListener: CurrentLocationListener

    override fun askSettingChangePermissionIfAny(locationGranterCallback: (Boolean) -> Unit) {
        locationGranterCallback(true)
    }

    override fun getContextListener(): ContextListener<LocationContext> {
        return currentLocationListener
    }

    @Inject
    lateinit var audioManager: AudioManager

    override fun getView(context: Context): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loc_smart_setting, null)

        val switchView = view.findViewById<Switch>(R.id.switch_view)

        switchView.isChecked = isRunning()

        switchView.setOnCheckedChangeListener { _, isEnable ->
            if (isEnable && !isRunning()) {
                start()
            } else if (!isEnable && isRunning()) {
                stop()
            }
        }

        return view
    }

    override fun applyChanges(settingData: VolumeActionData) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
            settingData.volumeToBeSet
        )
    }

    override fun criteriaMatching(criteria: LocationData, contextData: LocationContext): Boolean {
        if (LocationUtils.getDistanceInMetre(
                Pair(contextData.lat, contextData.lon),
                Pair(criteria.lat, criteria.lon)
            ) < criteria.radiusInMetre
        ) {
            return true
        }

        return false
    }
}