package com.smartsettings.ai.core.smartSettings

import android.util.Log
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.contextListeners.CurrentLocationListener
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.utils.LocationUtils
import javax.inject.Inject

class LocationBasedSmartSetting(name: String, locationData: LocationData) :
    SmartSetting<LocationData>(
        name,
        SerializableData(locationData)
    ) {

    @Inject
    lateinit var currentLocationListener: CurrentLocationListener


    override fun createContextListeners(): Set<ContextListener> {
        return setOf(currentLocationListener)
    }

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun criteriaMatchingForContextDataFromListener(
        criteria: LocationData,
        contextListener: ContextListener
    ): Boolean {

        if (contextListener is CurrentLocationListener) {

            val contextData = contextListener.getContextData()

            if (contextData != null) {
                if (LocationUtils.getDistanceInMetre(
                        Pair(contextData.lat, contextData.lon),
                        Pair(criteria.lat, criteria.lon)
                    ) < criteria.radiusInMetre
                ) {
                    Log.d("XDFCE", "criteria matched")
                    return true
                }
            }
        }



        Log.d("XDFCE", "criteria failed")

        return false
    }
}