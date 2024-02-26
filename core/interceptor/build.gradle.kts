plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.withpeace.core.interceptor"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
}