dependencies {
    "implementation"(libs.findLibrary("coroutines.core").get())
    "implementation"(libs.findLibrary("coroutines.android").get())
    "testImplementation"(libs.findLibrary("coroutines.android").get())
    "testImplementation"(libs.findLibrary("coroutines.test").get())
}
