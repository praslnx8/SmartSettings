package com.smartsettings.ai.resources.cloud

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("schema")
    fun getSchemas(): Call<List<SmartSettingSchemaCloudData>>
}