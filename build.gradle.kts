import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish) apply false
}

subprojects {
    group = property("GROUP").toString()
    version = property("VERSION_NAME").toString()

    plugins.withId("org.gradle.maven-publish") {
        configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                pom {
                    name.set(property("POM_NAME").toString())
                    description.set(property("POM_DESCRIPTION").toString())
                    url.set(property("POM_URL").toString())

                    licenses {
                        license {
                            name.set(property("POM_LICENCE_NAME").toString())
                            url.set(property("POM_LICENCE_URL").toString())
                            distribution.set(property("POM_LICENSE_DIST").toString())
                        }
                    }

                    developers {
                        developer {
                            id.set(property("POM_DEVELOPER_ID").toString())
                            name.set(property("POM_DEVELOPER_NAME").toString())
                            url.set(property("POM_DEVELOPER_URL").toString())
                        }
                    }

                    scm {
                        url.set(property("POM_SCM_URL").toString())
                        connection.set(property("POM_SCM_CONNECTION").toString())
                        developerConnection.set(property("POM_SCM_DEV_CONNECTION").toString())
                    }
                }
            }
        }
    }
}

// Dokka v2 多模块聚合：v1 的 dokkaHtmlMultiModule 隐式收集已移除，需显式声明聚合依赖
dependencies {
    dokka(project(":refresh"))
    dokka(project(":refresh-indicator-classic"))
    dokka(project(":refresh-indicator-progress"))
    dokka(project(":refresh-indicator-lottie"))
}
