
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
 ---
 
## 📋 CAT 2 Requirements & Implementation

The Scholarship Discovery System implements the following key requirements:

1. **User Profile Management**: Students can create and manage academic profiles containing their GPA, course of study, education level, and country.
2. **Scholarship Discovery**: Users can browse and search available scholarships using different filters.
3. **Eligibility Assessment**: The system evaluates scholarship requirements against student profiles and displays eligibility statuses (`Eligible`, `Not Eligible`, `Needs Info`).
4. **Saved Scholarships**: Students can bookmark and manage scholarships they are interested in.
5. **Application Tracking**: Users can record, update, and monitor scholarship applications through different stages (`Planning`, `In Progress`, `Submitted`, `Successful`, `Unsuccessful`).
6. **Deadline Notifications**: WorkManager schedules background reminders to notify students about upcoming scholarship deadlines.
7. **Data Persistence**: Room Database stores user profiles, scholarships, saved scholarships, and application records locally

   ---

## 🏗️ System Architecture

The application follows the **Model-View-ViewModel (MVVM)** architectural pattern to separate user interface components from business logic and data management.

### Architecture Components

- **UI Layer**: XML layouts and Android UI components display information and receive user input.
- **ViewModel Layer**: Manages UI-related data and handles communication between the UI and repositories.
- **Repository Layer**: Coordinates data operations and acts as an intermediary between ViewModels and data sources.
- **Local Database**: Room Database provides persistent storage for user profiles, scholarships, saved scholarships, and application records.
- **Remote Data Source**: Retrofit facilitates communication with remote APIs.
- **Background Processing**: WorkManager handles scheduled scholarship deadline reminders.

### Data Flow

`UI Layer` → `ViewModel` → `Repository` → `Room Database / Retrofit API`

---

## 👥 Team Member Contributions
The Scholarship Discovery System was developed collaboratively, with each team member assigned specific responsibilities to ensure successful implementation and integration of the application.

1. **Member 1 – Core Application Development**: Responsible for developing the core application components and foundation.
2. **Member 2 – Remote API Networking (Retrofit)**: Responsible for implementing the networking layer using Retrofit to retrieve online scholarship opportunities in JSON format.
3. **Member 3 – Eligibility Matching Engine**: Responsible for developing the eligibility matching logic that compares student profiles against scholarship requirements.
4. **Member 4 – Background Notifications (WorkManager)**: Responsible for implementing background processing to check scholarship deadlines and send local reminders.
5. **Member 5 – Project Documentation**: Responsible for preparing and maintaining project documentation, including the project overview, CAT 2 requirements, system architecture, and team member contributions.

Each member worked on their assigned feature using a dedicated Git feature branch. Contributions were submitted through GitHub Pull Requests for review and integration into the main branch.
   
