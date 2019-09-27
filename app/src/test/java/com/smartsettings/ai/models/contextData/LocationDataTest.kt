package com.smartsettings.ai.models.contextData

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationDataTest {

    @Test
    fun test_location_data_gives_correct_changed_data() {
        val locationData = LocationData(12.12, 80.80)
        assertEquals(Pair(12.12, 80.80), locationData.getChangedData())
    }
}