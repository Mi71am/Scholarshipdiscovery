# 🎓 Scholarship Discovery System
> **BBIT CAT 2 — Android Mobile Application Development Project**  
> **School of Computing and Engineering Sciences, Strathmore University**  
> **Theme 3**: Education Access and Equity  
> **GitHub Repository**: [https://github.com/Mi71am/Scholarshipdiscovery](https://github.com/Mi71am/Scholarshipdiscovery)

---

## 📌 Project Overview & Objective

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

---

## 👥 Group Collaboration & Contribution Stages (5 Members)

| Stage | Module / Responsibility | Primary Contributor | Key Components |
|---|---|---|---|
| **Member 1** | Application Data Models & Design Tokens | **Miriam** | Data Models (`UserProfile`, `Scholarship`, `SavedScholarship`, `ApplicationRecord`), `colors.xml`, `themes.xml`, `strings.xml`, Gradle build setup |
| **Member 2** | Room Local Database & DAOs | **Member 2** | `ScholarshipDao`, `UserProfileDao`, `SavedScholarshipDao`, `ApplicationRecordDao`, `ScholarshipDatabase` |
| **Member 3** | Repository, API & Eligibility Engine | **Member 3** | `ScholarshipRepository`, `ScholarshipApiService`, `RetrofitClient`, `EligibilityMatcher` |
| **Member 4** | Authentication & Navigation Architecture | **Member 4** | `LoginActivity`, `RegisterActivity`, `MainActivity`, `nav_graph.xml`, `bottom_nav_menu.xml` |
| **Member 5** | Feature Fragments, Adapters & Documentation | **Member 5** | `ScholarshipViewModel`, `DashboardFragment`, `DiscoverFragment`, `ScholarshipDetailsFragment`, `SavedScholarshipsFragment`, `ApplicationTrackerFragment`, `DeadlineReminderWorker`, `README.md` |

---

## 🚀 How to Run the App

1. Clone the repository:
   ```bash
   git clone https://github.com/Mi71am/Scholarshipdiscovery.git
   ```
2. Open the project in **Android Studio 2026.1.4+**.
3. Select an Android Emulator (API 24+) or connected physical device.
4. Click **Run 'app'** (`Shift + F10`).
