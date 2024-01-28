pluginManagement {
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

rootProject.name = "Instagram"
include(":app")
include(":core:util")
include(":core:ui")
include(":feature:auth")
include(":feature:home")
include(":data:firebase")
