plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
}

android {
    namespace = "com.withpeace.withpeace.core.database"
}

dependencies {
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    kapt (libs.room.compiler)
}