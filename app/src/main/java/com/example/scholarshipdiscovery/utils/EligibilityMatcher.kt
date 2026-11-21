package com.example.scholarshipdiscovery.utils

import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.data.model.UserProfile

enum class EligibilityStatus(val label: String) {
    ELIGIBLE("Eligible"),
    NOT_ELIGIBLE("Not Eligible"),
    NEEDS_MORE_INFO("Needs More Info")
}

object EligibilityMatcher {

    fun match(profile: UserProfile?, scholarship: Scholarship): EligibilityStatus {
        if (profile == null || profile.fullName.isEmpty()) {
            return EligibilityStatus.NEEDS_MORE_INFO
        }

        val levelMatches = scholarship.eligibleEducationLevel.equals("All", ignoreCase = true) ||
                scholarship.eligibleEducationLevel.equals(profile.educationLevel, ignoreCase = true)

        val countryMatches = scholarship.eligibleCountry.equals("Global", ignoreCase = true) ||
                scholarship.eligibleCountry.equals(profile.country, ignoreCase = true)

        val fieldMatches = scholarship.eligibleFieldOfStudy.equals("All", ignoreCase = true) ||
                scholarship.eligibleFieldOfStudy.contains(profile.fieldOfStudy, ignoreCase = true) ||
                profile.fieldOfStudy.contains(scholarship.eligibleFieldOfStudy, ignoreCase = true)

        val gpaMatches = profile.gpa >= scholarship.minGpa

        return if (levelMatches && countryMatches && fieldMatches && gpaMatches) {
            EligibilityStatus.ELIGIBLE
        } else if (!gpaMatches || (!levelMatches && !countryMatches)) {
            EligibilityStatus.NOT_ELIGIBLE
        } else {
            EligibilityStatus.NEEDS_MORE_INFO
        }
    }
}
