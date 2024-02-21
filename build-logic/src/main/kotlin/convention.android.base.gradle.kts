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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
}

dependencies{
    "androidTestImplementation"(libs.findLibrary("androidx.test.ext").get())
    "androidTestImplementation"(libs.findLibrary("androidx-test-espresso-core").get())
    "androidTestImplementation"(libs.findLibrary("junit4").get())
    "debugImplementation"(libs.findLibrary("androidx-test-core").get())
}
