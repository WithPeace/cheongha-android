import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalPropertyString(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

plugins {
    id("com.android.library")
    id("convention.android.compose")
    id("convention.android.hilt")
    id("convention.coroutine")
}

android {
    namespace = "com.withpeace.withpeace.googlelogin"

    defaultConfig {
        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            getLocalPropertyString("GOOGLE_CLIENT_ID"),
        )
    }
}

dependencies {
    implementation(libs.google.login)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.service)
}
