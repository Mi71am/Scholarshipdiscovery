package com.example.scholarshipdiscovery.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scholarshipdiscovery.data.model.Scholarship

@Dao
interface ScholarshipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScholarship(scholarship: Scholarship)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScholarships(scholarships: List<Scholarship>)

    @Update
    suspend fun updateScholarship(scholarship: Scholarship)

    @Delete
    suspend fun deleteScholarship(scholarship: Scholarship)

    @Query("SELECT * FROM scholarships ORDER BY id DESC")
    fun getAllScholarships(): LiveData<List<Scholarship>>

    @Query("SELECT * FROM scholarships WHERE id = :id")
    suspend fun getScholarshipById(id: Int): Scholarship?

    @Query("SELECT * FROM scholarships WHERE id IN (:ids)")
    fun getScholarshipsByIds(ids: List<Int>): LiveData<List<Scholarship>>
}
