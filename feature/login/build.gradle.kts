plugins {
    id("convention.feature")
}

android {
    namespace = "com.withpeace.login"
}

dependencies {
    implementation(project(":google-login"))
}