package com.smartsettings.ai.core.settingChangers

import android.media.AudioManager
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.data.actionData.VolumeActionData
import javax.inject.Inject

open class VolumeSettingChanger(volumeActionData: VolumeActionData) :
    SettingChanger<VolumeActionData>(SerializableData(volumeActionData)) {

    @Inject
    lateinit var audioManager: AudioManager

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun askSettingChangePermissionIfAny(permissionGrantCallback: (Boolean) -> Unit) {
        permissionGrantCallback(true)
    }

    override fun isPermissionGranted(): Boolean {
        return true
    }

    override fun applySettingChanges(actionData: VolumeActionData): Boolean {
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            serializableActionData.data.volumeToBeSet,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        )

        return true
    }

    class MuteSettingChanger : VolumeSettingChanger(VolumeActionData(0))
}