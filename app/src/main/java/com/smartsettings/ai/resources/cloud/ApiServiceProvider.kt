package com.smartsettings.ai.resources.cloud

import com.smartsettings.ai.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.API_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}