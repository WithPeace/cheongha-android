plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.home"
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}