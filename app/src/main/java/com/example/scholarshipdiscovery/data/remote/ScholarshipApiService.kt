package com.example.scholarshipdiscovery.data.remote

import com.example.scholarshipdiscovery.data.model.Scholarship
import retrofit2.Response
import retrofit2.http.GET

interface ScholarshipApiService {

    // Original function, kept unchanged
    @GET("scholarships.json")
    suspend fun fetchScholarships(): Response<List<Scholarship>>

    // Safer version: reads each entry as a ScholarshipDto, then maps it to Scholarship
    @GET("scholarships.json")
    suspend fun getScholarships(): List<ScholarshipDto>
}

/**
 * One scholarship entry as it appears in the JSON. Fields are nullable because
 * Gson ignores Kotlin default values for missing fields. It is mapped onto the
 * existing Scholarship model, so there is no duplicate model.
 */
data class ScholarshipDto(
    val title: String? = null,
    val provider: String? = null,
    val description: String? = null,
    val category: String? = null,
    val eligibleCountry: String? = null,
    val eligibleEducationLevel: String? = null,
    val eligibleFieldOfStudy: String? = null,
    val minGpa: Double? = null,
    val fundingType: String? = null,
    val amount: String? = null,
    val deadline: String? = null,
    val websiteUrl: String? = null,
    val contactPhone: String? = null,
    val imageUrl: String? = null,
    val benefits: String? = null,
    val requiredDocuments: String? = null
) {
    fun toScholarship(): Scholarship? {
        if (title.isNullOrBlank() || provider.isNullOrBlank()) return null

        val base = Scholarship(
            title = title,
            provider = provider,
            description = description ?: "",
            category = category ?: "General"
        )
        return base.copy(
            eligibleCountry = eligibleCountry ?: base.eligibleCountry,
            eligibleEducationLevel = eligibleEducationLevel ?: base.eligibleEducationLevel,
            eligibleFieldOfStudy = eligibleFieldOfStudy ?: base.eligibleFieldOfStudy,
            minGpa = minGpa ?: base.minGpa,
            fundingType = fundingType ?: base.fundingType,
            amount = amount ?: base.amount,
            deadline = deadline ?: base.deadline,
            websiteUrl = websiteUrl ?: base.websiteUrl,
            contactPhone = contactPhone ?: base.contactPhone,
            imageUrl = imageUrl ?: base.imageUrl,
            benefits = benefits ?: base.benefits,
            requiredDocuments = requiredDocuments ?: base.requiredDocuments
        )
    }
}