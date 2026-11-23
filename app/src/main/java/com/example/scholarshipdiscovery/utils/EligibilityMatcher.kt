package com.example.scholarshipdiscovery.utils

import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.data.model.UserProfile

enum class EligibilityStatus(val label: String) {
    ELIGIBLE("Eligible"),
    NOT_ELIGIBLE("Not Eligible"),
    NEEDS_MORE_INFO("Needs More Info")
}

data class EligibilityResult(
    val status: EligibilityStatus,
    val matchScore: Int, // 0 to 100
    val matchedCriteria: List<String>,
    val failedCriteria: List<String>,
    val missingProfileFields: List<String>
)

object EligibilityMatcher {

    /**
     * Primary status check used across the UI.
     */
    fun match(profile: UserProfile?, scholarship: Scholarship): EligibilityStatus {
        return evaluate(profile, scholarship).status
    }

    /**
     * Full dynamic evaluation returning status, percentage score, and criteria breakdown.
     */
    fun evaluate(profile: UserProfile?, scholarship: Scholarship): EligibilityResult {
        if (profile == null) {
            return EligibilityResult(
                status = EligibilityStatus.NEEDS_MORE_INFO,
                matchScore = 0,
                matchedCriteria = emptyList(),
                failedCriteria = emptyList(),
                missingProfileFields = listOf("Full Profile")
            )
        }

        val missingFields = mutableListOf<String>()
        val matched = mutableListOf<String>()
        val failed = mutableListOf<String>()

        val userGpa = profile.gpa.toDouble()
        val requiredGpa = scholarship.minGpa.toDouble()

        // 1. Check for missing profile fields
        if (profile.fullName.isBlank()) missingFields.add("Full Name")
        if (profile.educationLevel.isBlank()) missingFields.add("Education Level")
        if (profile.country.isBlank()) missingFields.add("Country")
        if (profile.fieldOfStudy.isBlank()) missingFields.add("Field of Study")
        if (userGpa <= 0.0 && requiredGpa > 0.0) missingFields.add("GPA")

        // 2. Evaluate Education Level
        if (profile.educationLevel.isNotBlank()) {
            if (matchesListOrWildcard(profile.educationLevel, scholarship.eligibleEducationLevel, listOf("All"))) {
                matched.add("Education Level (${scholarship.eligibleEducationLevel})")
            } else {
                failed.add("Requires ${scholarship.eligibleEducationLevel}")
            }
        }

        // 3. Evaluate Country
        if (profile.country.isNotBlank()) {
            if (matchesListOrWildcard(profile.country, scholarship.eligibleCountry, listOf("Global", "All"))) {
                matched.add("Country (${scholarship.eligibleCountry})")
            } else {
                failed.add("Restricted to ${scholarship.eligibleCountry}")
            }
        }

        // 4. Evaluate Field of Study
        if (profile.fieldOfStudy.isNotBlank()) {
            if (matchesFieldOfStudy(profile.fieldOfStudy, scholarship.eligibleFieldOfStudy)) {
                matched.add("Field of Study (${scholarship.eligibleFieldOfStudy})")
            } else {
                failed.add("Requires ${scholarship.eligibleFieldOfStudy}")
            }
        }

        // 5. Evaluate GPA
        if (requiredGpa <= 0.0 || userGpa > 0.0) {
            if (userGpa >= requiredGpa) {
                matched.add("GPA (${profile.gpa} >= ${scholarship.minGpa})")
            } else {
                failed.add("Minimum GPA required: ${scholarship.minGpa}")
            }
        }

        // Calculate score across the 4 core criteria
        val totalCriteria = 4
        val score = ((matched.size.toDouble() / totalCriteria) * 100).toInt()

        // Determine final status:
        // - Any confirmed requirement failure -> NOT_ELIGIBLE
        // - No failures, but required profile info is missing -> NEEDS_MORE_INFO
        // - All 4 criteria satisfied -> ELIGIBLE
        val status = when {
            failed.isNotEmpty() -> EligibilityStatus.NOT_ELIGIBLE
            missingFields.isNotEmpty() -> EligibilityStatus.NEEDS_MORE_INFO
            else -> EligibilityStatus.ELIGIBLE
        }

        return EligibilityResult(
            status = status,
            matchScore = score,
            matchedCriteria = matched,
            failedCriteria = failed,
            missingProfileFields = missingFields
        )
    }

    /**
     * Filters a list of scholarships down to those the student is eligible for.
     */
    fun filterEligible(profile: UserProfile?, scholarships: List<Scholarship>): List<Scholarship> {
        return scholarships.filter { match(profile, it) == EligibilityStatus.ELIGIBLE }
    }

    /**
     * Sorts scholarships so Eligible items appear first, ranked by highest match score.
     */
    fun rankScholarships(profile: UserProfile?, scholarships: List<Scholarship>): List<Scholarship> {
        return scholarships.sortedByDescending { evaluate(profile, it).matchScore }
    }

    private fun matchesListOrWildcard(
        userValue: String,
        requirement: String,
        wildcards: List<String>
    ): Boolean {
        val cleanReq = requirement.trim()
        if (cleanReq.isEmpty() || wildcards.any { cleanReq.equals(it, ignoreCase = true) }) {
            return true
        }
        val cleanUser = userValue.trim()
        val allowedValues = cleanReq.split(",").map { it.trim() }
        return allowedValues.any { it.equals(cleanUser, ignoreCase = true) }
    }

    private fun matchesFieldOfStudy(userField: String, requiredField: String): Boolean {
        val cleanReq = requiredField.trim()
        if (cleanReq.isEmpty() || cleanReq.equals("All", ignoreCase = true)) {
            return true
        }
        val cleanUser = userField.trim()
        if (cleanUser.isEmpty()) return false

        val allowedFields = cleanReq.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        return allowedFields.any { allowed ->
            allowed.equals(cleanUser, ignoreCase = true) ||
                    allowed.contains(cleanUser, ignoreCase = true) ||
                    cleanUser.contains(allowed, ignoreCase = true)
        }
    }
}