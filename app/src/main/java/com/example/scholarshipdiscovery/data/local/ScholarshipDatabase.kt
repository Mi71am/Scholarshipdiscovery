package com.example.scholarshipdiscovery.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.scholarshipdiscovery.data.model.ApplicationRecord
import com.example.scholarshipdiscovery.data.model.SavedScholarship
import com.example.scholarshipdiscovery.data.model.Scholarship
import com.example.scholarshipdiscovery.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Scholarship::class,
        UserProfile::class,
        SavedScholarship::class,
        ApplicationRecord::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ScholarshipDatabase : RoomDatabase() {

    abstract fun scholarshipDao(): ScholarshipDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun savedScholarshipDao(): SavedScholarshipDao
    abstract fun applicationRecordDao(): ApplicationRecordDao

    companion object {
        @Volatile
        private var INSTANCE: ScholarshipDatabase? = null

        fun getDatabase(context: Context): ScholarshipDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    ScholarshipDatabase::class.java,
                    "scholarship_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(ScholarshipDatabaseCallback(context.applicationContext))
                .build()
                INSTANCE = database
                database
            }
        }
    }

    private class ScholarshipDatabaseCallback(
        private val context: Context
    ) : Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(database: ScholarshipDatabase) {
            val userProfileDao = database.userProfileDao()
            val scholarshipDao = database.scholarshipDao()
            val applicationRecordDao = database.applicationRecordDao()

            if (userProfileDao.getUserProfileDirect() == null) {
                userProfileDao.saveUserProfile(
                    UserProfile(
                        id = 1,
                        fullName = "Miriam Student",
                        email = "student@strathmore.edu",
                        country = "Kenya",
                        institution = "Strathmore University",
                        fieldOfStudy = "Engineering & IT",
                        educationLevel = "Undergraduate",
                        gpa = 3.6,
                        financialNeed = "High"
                    )
                )
            }

            val sampleScholarships = listOf(
                Scholarship(
                    title = "STEM Future Leaders Grant",
                    provider = "Global Science Foundation",
                    description = "Full tuition grant for undergraduate students pursuing degrees in Computer Science, Data Science, or Engineering.",
                    category = "Engineering & IT",
                    eligibleCountry = "Global",
                    eligibleEducationLevel = "Undergraduate",
                    eligibleFieldOfStudy = "Engineering & IT",
                    minGpa = 3.2,
                    fundingType = "Full Tuition",
                    amount = "150000",
                    deadline = "2026-12-15",
                    websiteUrl = "https://www.strathmore.edu",
                    contactPhone = "+254712345678",
                    imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=600&auto=format&fit=crop",
                    benefits = "Full tuition coverage, KSh 15,000 monthly stipend, computer hardware grant",
                    requiredDocuments = "Academic transcripts, Recommendation letter, Personal statement, ID copy"
                ),
                Scholarship(
                    title = "Academic Excellence Bursary",
                    provider = "Strathmore Education Trust",
                    description = "Awarded to high-achieving undergraduate students with a cumulative GPA of 3.5 or above.",
                    category = "General Merit",
                    eligibleCountry = "Kenya",
                    eligibleEducationLevel = "Undergraduate",
                    eligibleFieldOfStudy = "All",
                    minGpa = 3.5,
                    fundingType = "Partial Grant",
                    amount = "100000",
                    deadline = "2026-11-30",
                    websiteUrl = "https://www.strathmore.edu",
                    contactPhone = "+254722998877",
                    imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=600&auto=format&fit=crop",
                    benefits = "KSh 100,000 tuition grant per academic semester",
                    requiredDocuments = "Official Strathmore transcript, Student ID, Proof of financial need"
                ),
                Scholarship(
                    title = "Women in Tech Scholarship",
                    provider = "TechEmpower Africa",
                    description = "Financial aid and mentorship program designed for female students enrolled in technology and innovation programs.",
                    category = "Technology",
                    eligibleCountry = "Kenya",
                    eligibleEducationLevel = "Undergraduate",
                    eligibleFieldOfStudy = "Engineering & IT",
                    minGpa = 3.0,
                    fundingType = "Full Tuition",
                    amount = "120000",
                    deadline = "2026-10-20",
                    websiteUrl = "https://www.strathmore.edu",
                    contactPhone = "+254733445566",
                    imageUrl = "https://images.unsplash.com/photo-1531482615713-2afd69097998?q=80&w=600&auto=format&fit=crop",
                    benefits = "Full tuition grant, 1-on-1 industry mentorship, internship placement opportunity",
                    requiredDocuments = "Academic record, Motivation letter, Female leadership statement"
                ),
                Scholarship(
                    title = "East Africa Leadership Fellowship",
                    provider = "Africa Foundation",
                    description = "Postgraduate research fellowship for outstanding student leaders pursuing master degrees in business or technology.",
                    category = "Leadership",
                    eligibleCountry = "Kenya",
                    eligibleEducationLevel = "Postgraduate",
                    eligibleFieldOfStudy = "Business",
                    minGpa = 3.8,
                    fundingType = "Full Grant",
                    amount = "250000",
                    deadline = "2026-09-01",
                    websiteUrl = "https://www.strathmore.edu",
                    contactPhone = "+254700112233",
                    imageUrl = "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?q=80&w=600&auto=format&fit=crop",
                    benefits = "KSh 250,000 research fellowship, accommodation stipend, international conference travel",
                    requiredDocuments = "Master thesis proposal, Undergraduate degree certificate, 2 Academic references"
                )
            )

            scholarshipDao.insertScholarships(sampleScholarships)

            if (applicationRecordDao.getAllApplications().value.isNullOrEmpty()) {
                applicationRecordDao.insertApplication(
                    ApplicationRecord(
                        scholarshipTitle = "STEM Future Leaders Grant",
                        deadline = "2026-12-15",
                        dateAdded = "2026-08-10",
                        status = "In progress",
                        notes = "Gathering transcript and reference letters from dean."
                    )
                )
            }
        }
    }
}
