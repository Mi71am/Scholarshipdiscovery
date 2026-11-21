// Top-level build file for Scholarship Discovery
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Safe Args for Navigation Component
    id("androidx.navigation.safeargs.kotlin") version "2.8.5" apply false

    // KSP for Room code generation
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}
