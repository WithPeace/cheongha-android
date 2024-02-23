plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
    id("convention.coroutine")
    id("kotlinx-serialization")
}

android {
    namespace = "com.withpeace.withpeace.core.network"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.skydoves.sandwich)
}