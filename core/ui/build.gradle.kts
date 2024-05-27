plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.compose")
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.withpeace.core.ui"
}

dependencies {
    implementation(project(":core:permission"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))
    implementation(libs.skydoves.landscapist.glide)
    implementation(libs.skydoves.landscapist.bom)
    implementation(libs.kotlinx.serialization.json)
}
