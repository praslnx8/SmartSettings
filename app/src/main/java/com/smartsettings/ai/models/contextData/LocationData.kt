package com.smartsettings.ai.models.contextData

import android.location.Location

class LocationData(private val location: Location) : ContextData<Location> {

    override fun getChangedData(): Location {
        return location
    }
}
