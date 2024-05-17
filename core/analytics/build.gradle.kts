plugins {
    id("com.android.library")
    id("convention.android.base")
}

android {
    namespace = "com.withpeace.withpeace.core.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}