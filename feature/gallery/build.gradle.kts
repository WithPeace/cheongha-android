plugins{
    id("convention.feature")
}

android{
    namespace = "com.withpeace.withpeace.feature.gallery"
    testOptions {
        unitTests.isReturnDefaultValues = true
        //https://developer.android.com/reference/tools/gradle-api/4.1/com/android/build/api/dsl/UnitTestOptions#isreturndefaultvalues
    }
}

dependencies{
    implementation(libs.skydoves.landscapist.bom)
    implementation(libs.skydoves.landscapist.glide)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.pagingCompose)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.core.testing)
}
