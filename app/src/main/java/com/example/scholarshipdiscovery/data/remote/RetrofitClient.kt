package com.example.scholarshipdiscovery.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://raw.githubusercontent.com/Mi71am/Scholarshipdiscovery/main/"

    val apiService: ScholarshipApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScholarshipApiService::class.java)
    }
}
