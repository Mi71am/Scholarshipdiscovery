package com.example.scholarshipdiscovery.data.remote

import com.example.scholarshipdiscovery.data.model.Scholarship
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://raw.githubusercontent.com/Mi71am/Scholarshipdiscovery/main/"

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ScholarshipApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScholarshipApiService::class.java)
    }

    suspend fun loadScholarships(): Result<List<Scholarship>> {
        return try {
            val scholarships = apiService.getScholarships().mapNotNull { it.toScholarship() }
            Result.success(scholarships)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}