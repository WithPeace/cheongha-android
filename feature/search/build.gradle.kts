plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.search"
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
}