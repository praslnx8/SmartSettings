package com.smartsettings.ai.utils

object LocationUtils {

    fun getDistance(loc1 : Pair<Double,Double>, loc2 : Pair<Double,Double>) : Int {
        return distance(loc1.first, loc1.second, loc2.first, loc2.second).toInt()
    }

    private fun distance(lat1 : Double, lon1 : Double, lat2 : Double, lon2: Double) : Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist *= 60 * 1.1515 * 1000
        return (dist)
    }

    private fun deg2rad(deg : Double) : Double {
        return (deg * Math.PI / 180.0);
    }

    private fun rad2deg(rad : Double) : Double {
        return (rad * 180.0 / Math.PI);
    }
}