package com.example.scholarshipdiscovery.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scholarshipdiscovery.data.model.SavedScholarship

@Dao
interface SavedScholarshipDao {

    @Query("SELECT * FROM saved_scholarships")
    fun getAllSavedScholarships(): LiveData<List<SavedScholarship>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveScholarship(saved: SavedScholarship)

    @Query("DELETE FROM saved_scholarships WHERE scholarshipId = :scholarshipId")
    suspend fun removeSavedScholarship(scholarshipId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_scholarships WHERE scholarshipId = :scholarshipId)")
    fun isScholarshipSaved(scholarshipId: Int): LiveData<Boolean>

    @Query("SELECT * FROM saved_scholarships")
    suspend fun getAllSavedScholarshipsList(): List<SavedScholarship>
}
