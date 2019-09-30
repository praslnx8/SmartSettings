package com.smartsettings.ai.models.smartSettings

import android.media.AudioManager
import android.util.Log
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.actionData.VolumeActionData
import com.smartsettings.ai.models.contextData.LocationContext
import com.smartsettings.ai.models.contextListeners.ContextListener
import com.smartsettings.ai.models.contextListeners.CurrentLocationListener
import com.smartsettings.ai.models.criteriaData.LocationData
import com.smartsettings.ai.models.serializables.SerializableData
import com.smartsettings.ai.utils.LocationUtils
import javax.inject.Inject

class LocationBasedVolumeSetting(name: String, locationData: LocationData, volumeActionData: VolumeActionData) :
    SmartSetting<LocationContext, LocationData, VolumeActionData>(
        name,
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

    override fun applyChanges(settingData: VolumeActionData) {
        Log.d("XDFCE", "changes applied")

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
            Log.d("XDFCE", "criteria matched")
            return true
        }

        Log.d("XDFCE", "criteria failed")

        return false
    }
}