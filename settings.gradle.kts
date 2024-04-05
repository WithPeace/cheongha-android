pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "withpeace"
include(":app")
include(":google-login")
include(":feature:login")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:datastore")
include(":core:designsystem")
include(":core:interceptor")
include(":core:testing")
include(":core:ui")
include(":core:imagestorage")
include(":core:permission")
include(":feature:signup")
include(":feature:home")
include(":feature:postlist")
include(":feature:postdetail")
include(":feature:registerpost")
include(":feature:mypage")
include(":feature:gallery")
include(":feature:profileeditor")
