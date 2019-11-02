package com.smartsettings.ai.core.settingChangers


enum class SettingChangerType(val value: String) {
    VOLUME_CHANGER("volume_changer"),
    VOLUME_MUTE_CHANGER("volume_mute_changer");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String): SettingChangerType? {
            for (i in values()) {
                if (i.value == value) {
                    return i
                }
            }

            return null
        }
    }
}