package com.example.scholarshipdiscovery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "scholarships")
data class Scholarship(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val provider: String,
    val description: String,
    val category: String,
    val eligibleCountry: String = "Global",
    val eligibleEducationLevel: String = "Undergraduate",
    val eligibleFieldOfStudy: String = "All",
    val minGpa: Double = 3.0,
    val fundingType: String = "Full Tuition",
    val amount: String = "150000",
    val deadline: String = "2026-12-31",
    val websiteUrl: String = "https://www.strathmore.edu",
    val contactPhone: String = "+254712345678",
    val imageUrl: String = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?q=80&w=600&auto=format&fit=crop",
    val benefits: String = "Full tuition coverage, KSh 15,000 monthly allowance, textbook grant",
    val requiredDocuments: String = "Academic transcripts, Copy of National ID, Recommendation letter, Personal statement"
) : Serializable {

    fun summary(): String {
        return "Scholarship: $title\nProvider: $provider\nCategory: $category\nFunding: KSh $amount\nDeadline: $deadline"
    }
}
