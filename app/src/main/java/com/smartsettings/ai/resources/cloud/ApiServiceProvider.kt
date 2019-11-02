package com.smartsettings.ai.resources.cloud

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    private const val API_BASE_URL = "http:localhost:8000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}