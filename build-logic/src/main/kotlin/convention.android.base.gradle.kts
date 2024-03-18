import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("android")
}
android {
    compileSdk = 34
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    "androidTestImplementation"(libs.findLibrary("androidx.test.ext").get())
    "androidTestImplementation"(libs.findLibrary("androidx-test-espresso-core").get())
    "androidTestImplementation"(libs.findLibrary("junit4").get())
    "debugImplementation"(libs.findLibrary("androidx-test-core").get())
}
