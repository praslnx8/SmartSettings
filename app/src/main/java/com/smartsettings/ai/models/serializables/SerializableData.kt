package com.smartsettings.ai.models.serializables

import com.google.gson.Gson

class SerializableData<T>(val data: T) {

    fun serialize(): String {
        return Gson().toJson(data)
    }
}