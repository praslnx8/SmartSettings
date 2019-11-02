package com.smartsettings.ai.resources.cloud

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("v1/schemas")
    fun getSchemas(): Call<List<SmartSettingSchemaCloudData>>
}