package com.smartsettings.ai.resources.cloud

import cloud.SmartSettingSchemaCloudData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("schema")
    fun getSchemas(): Call<List<SmartSettingSchemaCloudData>>
}