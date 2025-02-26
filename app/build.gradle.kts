plugins {
    id("convention.application")
    alias(libs.plugins.firebase.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.withpeace.withpeace"

    defaultConfig {
        applicationId = "com.withpeace.withpeace"
        targetSdk = 34
        versionCode = 18
        versionName = "2.3.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(project(":feature:login"))
    implementation(project(":feature:signup"))
    implementation(project(":feature:policyconsent"))
    implementation(project(":feature:privacypolicy"))
    implementation(project(":feature:termsofservice"))
    implementation(project(":feature:home"))
    implementation(project(":feature:postlist"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:registerpost"))
    implementation(project(":feature:gallery"))
    implementation(project(":feature:postdetail"))
    implementation(project(":feature:profileeditor"))
    implementation(project(":feature:policydetail"))
    implementation(project(":feature:policybookmarks"))
    implementation(project(":feature:disablepolicy"))
    implementation(project(":feature:policylist"))
    implementation(project(":feature:policyfilter"))
    implementation(project(":feature:search"))
    implementation(project(":feature:balancegame"))
    implementation(project(":core:ui"))
    implementation(project(":core:interceptor"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    testImplementation(project(":core:testing"))
}
