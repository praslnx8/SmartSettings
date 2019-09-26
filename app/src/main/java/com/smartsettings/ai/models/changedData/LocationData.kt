package com.smartsettings.ai.models.changedData

class LocationData(val lat: Double, val lon: Double) : ContextData {

    override fun getChangedData(): Pair<Double, Double> {
        return Pair(lat, lon)
    }
}
