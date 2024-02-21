import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

plugins {
    id("com.android.library")
    id("convention.android.compose")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.google_login"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "CLIENT_ID",
            getApiKey("SERVER_CLIENT_ID"),
        )
    }
}

dependencies {
    implementation(libs.google.login)
    implementation(libs.multidex)
}
