plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.withpeace.core.interceptor"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.skydoves.sandwich)
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
}
