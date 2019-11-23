package com.smartsettings.ai.data.criteriaData

data class LocationData(
    val lat: Double,
    val lon: Double,
    val radiusInMetre: Int,
    val isExitOrIn : Boolean //True for exit
)