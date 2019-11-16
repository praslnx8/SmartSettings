package core


enum class SettingChangerType(val value: String) {
    VOLUME_CHANGER("volume_changer");

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