import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getLocalPropertyString(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

plugins {
    id("com.android.library")
    id("convention.android.base")
    id("convention.android.hilt")
    id("convention.coroutine")
    id("convention.test.library")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "YOUTH_POLICY_API_KEY",
            getLocalPropertyString("YOUTH_POLICY_API_KEY"),
        )
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        //https://developer.android.com/reference/tools/gradle-api/4.1/com/android/build/api/dsl/UnitTestOptions#isreturndefaultvalues
    }
    namespace = "com.withpeace.withpeace.core.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:imagestorage"))
    implementation(project(":core:testing"))
    implementation(project(":core:analytics"))
    implementation(libs.skydoves.sandwich)
    implementation(libs.androidx.paging)
    testImplementation(libs.androidx.paging.testing)
    implementation("androidx.exifinterface:exifinterface:1.3.7")
}
