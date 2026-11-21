package com.example.scholarshipdiscovery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "",
    val email: String = "",
    val country: String = "Kenya",
    val institution: String = "",
    val fieldOfStudy: String = "Engineering & IT",
    val educationLevel: String = "Undergraduate",
    val gpa: Double = 3.5,
    val financialNeed: String = "High"
) : Serializable
