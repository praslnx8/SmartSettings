package core

enum class ContextListenerType(val value: String) {
    LOCATION_LISTENER("location_listener"),
    WIFI_LISTENER("wifi_listener");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String): ContextListenerType? {
            for (i in values()) {
                if (i.value == value) {
                    return i
                }
            }

            return null
        }
    }
}