plugins {
    id("convention.application")
}

android {
    namespace = "com.withpeace.withpeace"

    defaultConfig {
        applicationId = "com.withpeace.withpeace"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(project(":feature:login"))
    implementation(project(":core:interceptor"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
}
