package com.example.scholarshipdiscovery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_scholarships")
data class SavedScholarship(
    @PrimaryKey val scholarshipId: Int,
    val savedAt: Long = System.currentTimeMillis()
)
