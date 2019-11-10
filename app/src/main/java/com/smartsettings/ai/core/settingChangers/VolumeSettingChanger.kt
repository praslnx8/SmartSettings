package com.smartsettings.ai.core.settingChangers

import android.media.AudioManager
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.di.DependencyProvider

open class VolumeSettingChanger(volumeActionData: VolumeActionData) :
    SettingChanger<VolumeActionData>(SerializableData(volumeActionData)) {

    private val audioManager: AudioManager = DependencyProvider.audioManager

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