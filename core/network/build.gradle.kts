plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
    id("convention.coroutine")
    id("kotlinx-serialization")
}

android {
    namespace = "com.withpeace.withpeace.core.network"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.skydoves.sandwich)
    kapt(libs.tikxml.processor)
    implementation(libs.tikxml.core)
    implementation(libs.retrofit.tikxml.converter)
    implementation(libs.tikxml.annotation)
}
