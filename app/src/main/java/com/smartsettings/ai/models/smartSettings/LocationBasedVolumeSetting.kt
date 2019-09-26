package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Switch
import com.smartsettings.ai.R
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.changedData.ContextData
import com.smartsettings.ai.models.changedData.LocationData
import com.smartsettings.ai.utils.LocationUtils
import javax.inject.Inject

class LocationBasedVolumeSetting(
    private val lat: Double,
    private val lon: Double,
    private val radiusInMetre: Int,
    private val phoneVolumeToSet: Int
) : SmartSetting() {

    @Inject
    lateinit var audioManager: AudioManager

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun listenForChanges() {

    }

    override fun stopListeningChanges() {

    }

    override fun getView(context: Context): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loc_smart_setting, null)

        val switchView = view.findViewById<Switch>(R.id.switch_view)

        switchView.isChecked = isRunning()

        switchView.setOnCheckedChangeListener { _, isEnable ->
            if (isEnable && !isRunning()) {
                start()
            } else if (!isEnable && isRunning()) {
                stop()
            }
        }

        return view
    }

    override fun applyChanges() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
            phoneVolumeToSet
        )
    }

    override fun askPermissions() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun criteriaMatching(contextData: ContextData): Boolean {
        if (contextData is LocationData) {
            if (LocationUtils.getDistanceInMetre(
                    Pair(contextData.lat, contextData.lon),
                    Pair(lat, lon)
                ) < radiusInMetre
            ) {
                return true
            }
        }

        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationBasedVolumeSetting

        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (radiusInMetre != other.radiusInMetre) return false
        if (phoneVolumeToSet != other.phoneVolumeToSet) return false
        if (audioManager != other.audioManager) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + lon.hashCode()
        result = 31 * result + radiusInMetre
        result = 31 * result + phoneVolumeToSet
        result = 31 * result + audioManager.hashCode()
        return result
    }
}