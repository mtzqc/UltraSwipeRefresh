// 本地（非 CI）环境优先使用阿里云镜像加速依赖解析，官方仓库保留为回退

pluginManagement {
    repositories {
        if (System.getenv("CI") == null) {
            maven("https://maven.aliyun.com/repository/gradle-plugin")
            maven("https://maven.aliyun.com/repository/public")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        if (System.getenv("CI") == null) {
            maven("https://maven.aliyun.com/repository/google")
            maven("https://maven.aliyun.com/repository/public")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "UltraSwipeRefresh"
include(
    ":composeApp",
    ":refresh",
    ":refresh-indicator-classic",
    ":refresh-indicator-progress",
    ":refresh-indicator-lottie"
)
