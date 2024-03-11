plugins {
    id("convention.kotlin.library")
    id("convention.test.library")
}

dependencies {
    implementation(libs.inject)
    implementation(libs.androidx.paging.common) // without android dependencies paging
}
