package com.smartsettings.ai.resources.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingChangerConverter {

    @TypeConverter
    fun fromString(value: String): List<SettingChangerDBModel> {
        val listType = object : TypeToken<ArrayList<SettingChangerDBModel>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(settingChangerDBModels: List<SettingChangerDBModel>): String {
        return Gson().toJson(settingChangerDBModels)
    }
}

class ContextListenerConverter {

    @TypeConverter
    fun fromString(value: String): List<ContextListenerDBModel> {
        val listType = object : TypeToken<ArrayList<ContextListenerDBModel>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(contextListenerDBModels: List<ContextListenerDBModel>): String {
        return Gson().toJson(contextListenerDBModels)
    }
}