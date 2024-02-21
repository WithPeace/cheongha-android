plugins {
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

dependencies {
    "implementation"(libs.findLibrary("hilt.core").get())
    "implementation"(libs.findLibrary("hilt.android").get())
    "implementation"(libs.findLibrary("hilt.navigation.compose").get())
    "implementation"(libs.findLibrary("hilt.android.testing").get())
    "kapt"(libs.findLibrary("hilt.android.compiler").get())
    "kaptAndroidTest"(libs.findLibrary("hilt.android.compiler").get())
    "androidTestImplementation"(libs.findLibrary("hilt.android.testing").get())
}
