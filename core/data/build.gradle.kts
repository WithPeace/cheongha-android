plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
    id("convention.coroutine")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    namespace = "com.withpeace.withpeace.core.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:imagestorage"))
    implementation(libs.skydoves.sandwich)
    implementation(libs.androidx.paging)
}
