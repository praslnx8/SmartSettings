package com.smartsettings.ai.models.smartSettings

import android.media.AudioManager
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.SettingState
import com.smartsettings.ai.models.changedData.ChangedData
import com.smartsettings.ai.models.changedData.LocationData
import com.smartsettings.ai.utils.LocationUtils
import javax.inject.Inject

class LocationBasedVolumeSetting(
    private val lat: Double,
    private val lon: Double,
    private val radiusInMetre: Int,
    private val phoneVolumeToSet: Int
) : SmartSetting {

    @Inject
    lateinit var audioManager: AudioManager

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun applyChanges(currentState : SettingState): SettingState {
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
            phoneVolumeToSet
        )
        currentState.volume = phoneVolumeToSet
        return currentState
    }

    override fun askPermissions() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun criteriaMatching(changedData: ChangedData): Boolean {
        if(changedData is LocationData) {
            if (LocationUtils.getDistanceInMetre(
                    Pair(changedData.lat, changedData.lon),
                    Pair(lat, lon)
                ) < radiusInMetre
            ) {
                return true
            }
        }

        return false
    }
}