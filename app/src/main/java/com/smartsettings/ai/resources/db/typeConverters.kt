package com.smartsettings.ai.resources.db

import androidx.room.TypeConverter
import cloud.SchemaCategory
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

class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(contextListenerDBModels: List<String>): String {
        return Gson().toJson(contextListenerDBModels)
    }
}

class CategoryConverter {
    @TypeConverter
    fun fromString(value: String): SchemaCategory {
        return  SchemaCategory.valueOf(value)
    }

    @TypeConverter
    fun fromArrayList(schemaCategory: SchemaCategory): String {
        return schemaCategory.name
    }
}

class ContextListenerSchemasConverter {

    @TypeConverter
    fun fromString(value: String): List<ContextListenerSchemaDBModel> {
        val listType = object : TypeToken<ArrayList<ContextListenerSchemaDBModel>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(contextListenerSchemasDBModels: List<ContextListenerSchemaDBModel>): String {
        return Gson().toJson(contextListenerSchemasDBModels)
    }
}

class SettingChangerSchemasConverter {

    @TypeConverter
    fun fromString(value: String): List<SettingChangerSchemaDBModel> {
        val listType = object : TypeToken<ArrayList<SettingChangerSchemaDBModel>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(settingChangerSchemasDBModels: List<SettingChangerSchemaDBModel>): String {
        return Gson().toJson(settingChangerSchemasDBModels)
    }
}
