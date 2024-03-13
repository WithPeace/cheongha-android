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
    implementation(libs.androidx.core.splashscreen)
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:post"))
    implementation(project(":feature:mypage"))
    implementation(project(":core:interceptor"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    testImplementation(project(":core:testing"))
}
