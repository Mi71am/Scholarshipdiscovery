package com.example.scholarshipdiscovery.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.scholarshipdiscovery.data.local.ScholarshipDatabase
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.data.model.SavedScholarship
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.data.model.UserProfile
import com.example.scholarshipdiscovery.data.repository.ScholarshipRepository
import com.example.scholarshipdiscovery.utils.EligibilityMatcher
import com.example.scholarshipdiscovery.utils.EligibilityStatus
import kotlinx.coroutines.launch

class ScholarshipViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ScholarshipDatabase.getDatabase(application)
    private val repository = ScholarshipRepository(
        db.scholarshipDao(),
        db.userProfileDao(),
        db.savedScholarshipDao(),
        db.applicationRecordDao()
    )

    val userProfile: LiveData<UserProfile?> = repository.userProfile
    val allScholarships: LiveData<List<Scholarship>> = repository.scholarships
    val savedScholarships: LiveData<List<SavedScholarship>> = repository.savedScholarships
    val trackedApplications: LiveData<List<ApplicationRecord>> = repository.applicationRecords

    val searchQuery = MutableLiveData<String>("")
    val selectedCountry = MutableLiveData<String>("All")
    val selectedEducationLevel = MutableLiveData<String>("All")
    val selectedFieldOfStudy = MutableLiveData<String>("All")
    val selectedFundingType = MutableLiveData<String>("All")

    val apiStatusMessage = MutableLiveData<String?>(null)

    val savedScholarshipIds: LiveData<Set<Int>> = savedScholarships.map { list ->
        list.map { it.scholarshipId }.toSet()
    }

    val savedScholarshipsList: LiveData<List<Scholarship>> = MediatorLiveData<List<Scholarship>>().apply {
        var allList: List<Scholarship> = emptyList()
        var savedIds: Set<Int> = emptySet()

        fun update() {
            value = allList.filter { savedIds.contains(it.id) }
        }

        addSource(allScholarships) { list ->
            allList = list
            update()
        }
        addSource(savedScholarshipIds) { ids ->
            savedIds = ids
            update()
        }
    }

    val filteredScholarships = MediatorLiveData<List<Scholarship>>().apply {
        fun filter() {
            val list = allScholarships.value ?: emptyList()
            val query = searchQuery.value?.trim()?.lowercase() ?: ""
            val country = selectedCountry.value ?: "All"
            val level = selectedEducationLevel.value ?: "All"
            val field = selectedFieldOfStudy.value ?: "All"
            val funding = selectedFundingType.value ?: "All"

            value = list.filter { item ->
                val matchesQuery = query.isEmpty() ||
                        item.title.lowercase().contains(query) ||
                        item.provider.lowercase().contains(query) ||
                        item.category.lowercase().contains(query) ||
                        item.description.lowercase().contains(query)

                val matchesCountry = country == "All" || item.eligibleCountry.equals("Global", ignoreCase = true) || item.eligibleCountry.equals(country, ignoreCase = true)
                val matchesLevel = level == "All" || item.eligibleEducationLevel.equals("All", ignoreCase = true) || item.eligibleEducationLevel.equals(level, ignoreCase = true)
                val matchesField = field == "All" || item.eligibleFieldOfStudy.equals("All", ignoreCase = true) || item.eligibleFieldOfStudy.contains(field, ignoreCase = true)
                val matchesFunding = funding == "All" || item.fundingType.equals(funding, ignoreCase = true)

                matchesQuery && matchesCountry && matchesLevel && matchesField && matchesFunding
            }
        }

        addSource(allScholarships) { filter() }
        addSource(searchQuery) { filter() }
        addSource(selectedCountry) { filter() }
        addSource(selectedEducationLevel) { filter() }
        addSource(selectedFieldOfStudy) { filter() }
        addSource(selectedFundingType) { filter() }
    }

    init {
        refreshRemoteData()
    }

    fun refreshRemoteData() {
        viewModelScope.launch {
            val success = repository.fetchRemoteScholarships()
            if (!success) {
                apiStatusMessage.postValue("Showing offline cached opportunities.")
            } else {
                apiStatusMessage.postValue("Refreshed latest scholarships online.")
            }
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun toggleSaveScholarship(scholarshipId: Int) {
        viewModelScope.launch {
            val currentSaved = savedScholarshipIds.value ?: emptySet()
            if (currentSaved.contains(scholarshipId)) {
                repository.removeSavedScholarship(scholarshipId)
            } else {
                repository.saveScholarship(scholarshipId)
            }
        }
    }

    fun addApplication(record: ApplicationRecord) {
        viewModelScope.launch {
            repository.addApplication(record)
        }
    }

    fun updateApplication(record: ApplicationRecord) {
        viewModelScope.launch {
            repository.updateApplication(record)
        }
    }

    fun deleteApplication(record: ApplicationRecord) {
        viewModelScope.launch {
            repository.deleteApplication(record)
        }
    }

    fun checkEligibility(scholarship: Scholarship): EligibilityStatus {
        return EligibilityMatcher.match(userProfile.value, scholarship)
    }

    fun resetFilters() {
        searchQuery.value = ""
        selectedCountry.value = "All"
        selectedEducationLevel.value = "All"
        selectedFieldOfStudy.value = "All"
        selectedFundingType.value = "All"
    }
}
