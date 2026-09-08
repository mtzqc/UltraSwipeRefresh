import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    js(IR) {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":refresh"))
            api(libs.compottie)
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
        }
    }
}

compose {
    resources {
        packageOfResClass = "com.king.ultraswiperefresh.indicator.lottie.generated.resources"
    }
}

mavenPublishing {
    // 发布到 Maven Central（Central Portal）；CI 使用 publishAndReleaseToMavenCentral 完成发布+自动 Release
    publishToMavenCentral()
    // 签名由插件根据属性自动配置：CI 传 RELEASE_SIGNING_ENABLED=true 时自动启用签名，本地不传则不签名。
    // 勿在此手动调用 signAllPublications()：插件的自动配置路径会先 finalize signing 属性，二次 set 抛 IllegalStateException。
}

android {
    namespace = "com.king.ultraswiperefresh.indicator.lottie"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        targetSdk = libs.versions.targetSdk.get().toInt()
        abortOnError = false
    }
}
