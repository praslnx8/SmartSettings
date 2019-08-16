package com.smartsettings.ai.models.changedData

class LocationData(val lat:Double, val lon: Double) : ChangedData {

    override fun getChangedData(): Any {
        return Pair<Double,Double>(lat, lon);
    }
}
