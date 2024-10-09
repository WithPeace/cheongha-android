plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.policylist"
}

dependencies {
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
}