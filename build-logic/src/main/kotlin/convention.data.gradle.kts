plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.test.library")
    id("convention.coroutine")
    id("convention.android.hilt")
}

dependencies {
    implementation(project(":core:analytics"))
}
