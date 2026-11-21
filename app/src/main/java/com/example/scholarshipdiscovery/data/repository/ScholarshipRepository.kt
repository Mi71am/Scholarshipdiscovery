package com.example.scholarshipdiscovery.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.scholarshipdiscovery.data.local.ApplicationRecordDao
import com.example.scholarshipdiscovery.data.local.SavedScholarshipDao
import com.example.scholarshipdiscovery.data.local.ScholarshipDao
import com.example.scholarshipdiscovery.data.local.UserProfileDao
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.data.model.SavedScholarship
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.data.model.UserProfile
import com.example.scholarshipdiscovery.data.remote.RetrofitClient

class ScholarshipRepository(
    private val scholarshipDao: ScholarshipDao,
    private val userProfileDao: UserProfileDao,
    private val savedScholarshipDao: SavedScholarshipDao,
    private val applicationRecordDao: ApplicationRecordDao
) {

    val userProfile: LiveData<UserProfile?> = userProfileDao.getUserProfile()
    val scholarships: LiveData<List<Scholarship>> = scholarshipDao.getAllScholarships()
    val savedScholarships: LiveData<List<SavedScholarship>> = savedScholarshipDao.getAllSavedScholarships()
    val applicationRecords: LiveData<List<ApplicationRecord>> = applicationRecordDao.getAllApplications()

    suspend fun fetchRemoteScholarships(): Boolean {
        return try {
            val response = RetrofitClient.apiService.fetchScholarships()
            if (response.isSuccessful) {
                val list = response.body()
                if (!list.isNullOrEmpty()) {
                    scholarshipDao.insertScholarships(list)
                    true
                } else false
            } else false
        } catch (e: Exception) {
            Log.e("REPOSITORY_LOG", "API request failed, using cached Room data: ${e.message}")
            false
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun getUserProfileDirect(): UserProfile? {
        return userProfileDao.getUserProfileDirect()
    }

    suspend fun saveScholarship(scholarshipId: Int) {
        savedScholarshipDao.saveScholarship(SavedScholarship(scholarshipId = scholarshipId))
    }

    suspend fun removeSavedScholarship(scholarshipId: Int) {
        savedScholarshipDao.removeSavedScholarship(scholarshipId)
    }

    suspend fun addApplication(record: ApplicationRecord) {
        applicationRecordDao.insertApplication(record)
    }

    suspend fun updateApplication(record: ApplicationRecord) {
        applicationRecordDao.updateApplication(record)
    }

    suspend fun deleteApplication(record: ApplicationRecord) {
        applicationRecordDao.deleteApplication(record)
    }
}
