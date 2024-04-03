plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.withpeace.feature.profileeditor"
}

dependencies {

    implementation(project(":core:ui"))
}