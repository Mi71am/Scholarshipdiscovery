package com.example.scholarshipdiscovery.data.remote

import com.example.scholarshipdiscovery.data.model.Scholarship
import retrofit2.Response
import retrofit2.http.GET

interface ScholarshipApiService {

    @GET("scholarships.json")
    suspend fun fetchScholarships(): Response<List<Scholarship>>
}
