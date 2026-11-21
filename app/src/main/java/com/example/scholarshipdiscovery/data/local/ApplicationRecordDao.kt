package com.example.scholarshipdiscovery.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scholarshipdiscovery.data.model.ApplicationRecord

@Dao
interface ApplicationRecordDao {

    @Query("SELECT * FROM application_records ORDER BY id DESC")
    fun getAllApplications(): LiveData<List<ApplicationRecord>>

    @Insert
    suspend fun insertApplication(record: ApplicationRecord)

    @Update
    suspend fun updateApplication(record: ApplicationRecord)

    @Delete
    suspend fun deleteApplication(record: ApplicationRecord)
}
