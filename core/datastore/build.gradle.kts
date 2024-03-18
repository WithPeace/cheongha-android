plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.withpeace.core.datastore"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.androidx.datastore)
}
