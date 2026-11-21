package com.example.scholarshipdiscovery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "application_records")
data class ApplicationRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val scholarshipTitle: String,
    val deadline: String,
    val dateAdded: String,
    val status: String, // "Planning to apply", "In progress", "Submitted", "Successful", "Unsuccessful"
    val notes: String = ""
) : Serializable
