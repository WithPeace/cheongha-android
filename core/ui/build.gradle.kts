plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.compose")
}

android {
    namespace = "com.withpeace.withpeace.core.ui"
}

dependencies {
    implementation(project(":core:permission"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(libs.skydoves.landscapist.glide)
    implementation(libs.skydoves.landscapist.bom)
}
