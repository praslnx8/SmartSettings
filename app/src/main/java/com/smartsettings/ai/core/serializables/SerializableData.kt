package com.smartsettings.ai.core.serializables

import com.google.gson.Gson

class SerializableData<T>(val data: T) {

    fun serialize(): String {
        if (data is String) {
            return data
        }
        return Gson().toJson(data)
    }
}