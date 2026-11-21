
##  Project Overview & Objective

The **Scholarship Discovery System** is a native Android application built in Kotlin that helps university and college students discover financial aid opportunities tailored to their academic background, course of study, and financial preferences.

### Problem Statement
Students frequently miss out on scholarship opportunities because information is scattered across fragmented websites, social media channels, and bulletin boards. Manual searching is time-consuming and often leads to students applying for opportunities where they do not meet eligibility requirements.

### Proposed Solution
Our application aggregates scholarship opportunities into a single, organized platform featuring:
1. **Student Academic Profile**: Captures GPA, course of study, education level, and country.
2. **Dynamic Eligibility Engine**: Performs real-time eligibility comparisons (`Eligible`, `Not Eligible`, `Needs Info`).
3. **Discover Hub**: Provides multi-criteria search and horizontal filter chips (`Undergraduate`, `Engineering & IT`, `Full Tuition`).
4. **Saved Shortlist**: Bookmarking manager for interesting opportunities.
5. **Application Tracker**: Complete CRUD pipeline (`Planning`, `In Progress`, `Submitted`, `Successful`, `Unsuccessful`).
6. **WorkManager Reminders**: Scheduled deadline notifications.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0.21
- **Architecture**: MVVM (`UI Layer` $\rightarrow$ `ViewModel` $\rightarrow$ `Repository` $\rightarrow$ `Room DB` / `Retrofit API`)
- **UI Framework**: XML Layouts, Material3, View Binding, ConstraintLayout, RecyclerView, BottomNavigationView
- **Local Persistence**: Room Database (Entities: `UserProfile`, `Scholarship`, `SavedScholarship`, `ApplicationRecord`)
- **Networking**: Retrofit 2.11.0 + Gson Converter
- **Image Loading**: Coil 2.7.0 (HTTPS photography with `centerCrop` & placeholders)
- **Background Tasks**: WorkManager 2.9.1 (`DeadlineReminderWorker`)
- **Navigation**: Jetpack Navigation Component with SafeArgs




