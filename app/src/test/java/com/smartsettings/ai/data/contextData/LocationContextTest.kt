package com.smartsettings.ai.data.contextData

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationContextTest {

    @Test
    fun test_location_data_gives_correct_changed_data() {
        val locationData = LocationContext(12.12, 80.80)
        assertEquals(12.12, locationData.lat, 0.01)
        assertEquals(80.80, locationData.lon, 0.01)
    }
}