pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UltraSwipeRefresh"
include(
    ":app",
    ":refresh",
    ":refresh-indicator-classic",
    ":refresh-indicator-progress",
    ":refresh-indicator-lottie"
)
