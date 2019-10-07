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