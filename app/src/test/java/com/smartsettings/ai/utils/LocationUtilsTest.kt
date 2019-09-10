package com.smartsettings.ai.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LocationUtilsTest {
    @Test
    fun test_distance__for_given_lat_lon_points_are_correct() {

        val distance = LocationUtils.getDistanceInMetre(Pair(12.969206, 77.597546), Pair(12.982241, 80.245735))
        assertEquals(286935, distance)
    }
}
