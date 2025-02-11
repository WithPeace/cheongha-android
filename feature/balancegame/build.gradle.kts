plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.balancegame"
}

dependencies {
    implementation(libs.skydoves.landscapist.glide)
    implementation(libs.skydoves.landscapist.bom)
}