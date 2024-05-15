import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalPropertyString(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}


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
        buildConfigField(
            "String",
            "BASE_URL",
            getLocalPropertyString("BASE_URL"),
        )
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
