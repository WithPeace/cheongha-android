plugins {
   id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.postlist"
}

dependencies {
    implementation(libs.skydoves.landscapist.bom)
    implementation(libs.skydoves.landscapist.glide)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.core.testing)
}
