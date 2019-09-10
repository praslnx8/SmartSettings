package com.smartsettings.ai.utils

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object LocationUtils {

    fun getDistanceInMetre(loc1: Pair<Double, Double>, loc2: Pair<Double, Double>): Int {
        return distance(loc1.first, loc1.second, loc2.first, loc2.second).toInt()
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta)))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515 * 1000
        dist /= 0.62137
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}