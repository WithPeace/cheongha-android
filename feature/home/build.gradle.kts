plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.home"
}

dependencies {
    implementation(libs.skydoves.balloon)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
}