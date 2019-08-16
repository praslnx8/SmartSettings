package com.smartsettings.ai.models.smartSettings

import com.smartsettings.ai.models.SettingState
import com.smartsettings.ai.models.changedData.ChangedData
import com.smartsettings.ai.models.changedData.LocationData
import com.smartsettings.ai.utils.LocationUtils

class LocationBasedVolumeSetting(val lat: Double, val lon: Double, val radiusInMetre : Int, val phoneVolumeToSet : Int) : SmartSetting {

    override fun applyChanges(currentState : SettingState): SettingState {
        return currentState
    }

    override fun askPermissions() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCriteria(changedData: ChangedData): Boolean {
        if(changedData is LocationData) {
            if(LocationUtils.getDistance(Pair(changedData.lat, changedData.lon), Pair(lat, lon)) < radiusInMetre) {
                return true
            }
        }

        return false
    }
}