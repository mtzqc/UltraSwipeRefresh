# UltraSwipeRefresh KMP 迁移设计

- 日期：2026-09-05
- 状态：已定稿（用户确认）
- 主题：将 UltraSwipeRefresh（Android Compose 下拉刷新库）迁移为 Kotlin Multiplatform 项目，并支持发布到 GitHub Packages

## 1. 背景与现状

UltraSwipeRefresh 是纯 Compose 实现的下拉刷新/上拉加载库，当前为 Android-only 多模块项目：

- 模块：`refresh`（核心）、`refresh-indicator-classic`、`refresh-indicator-progress`、`refresh-indicator-lottie`、`app`（demo）
- 技术栈：Kotlin 1.9.20、AGP 8.6.0、AndroidX Compose BOM 2025.04.01（foundation 1.8.0）
- 发布：vanniktech maven-publish 0.34.0 → Maven Central，group `com.github.jenly1314.UltraSwipeRefresh`
- 源码中仅有的 Android 平台依赖：振动反馈（`Context`/`Vibrator`/`VibratorManager`/`Build`）与 `androidx.annotation.FloatRange/IntRange` 注解；其余全部为跨平台可用的 Compose 代码

## 2. 目标

1. 4 个库模块全部 KMP 化，支持平台：Android、iOS（arm64/x64/SimulatorArm64）、Desktop JVM（Windows/macOS/Linux）、macOS（arm64/x64）、JS（IR）、WasmJS
2. 发布目标改为 GitHub Packages（`https://maven.pkg.github.com/jenly1314/UltraSwipeRefresh`）
3. `app` demo KMP 化为 `composeApp`，Android 端功能不变，新增其他平台入口
4. artifactId 全部保持不变，group 不变，现有 Android 消费者坐标无需变更（只需添加仓库）

## 3. 非目标

- 不迁移到 Maven Central（现有 Central 产物保留不动，本次不更新）
- 不重构核心 API（`UltraSwipeRefresh`、State、Theme、Indicator 接口签名不变）
- 不引入 build-logic convention plugins（当前 5 个模块规模下直接配置各模块 build 脚本，避免过度设计）

## 4. 版本底座

| 项 | 现状 | 目标 | 说明 |
|---|---|---|---|
| Kotlin | 1.9.20 | 2.2.x（如 2.2.21） | 全平台 Compose 必须 Kotlin 2.x |
| Compose 编译器 | `composeOptions.kotlinCompilerExtensionVersion` | `org.jetbrains.kotlin.plugin.compose` 内置插件 | Kotlin 2.0 起编译器随 Kotlin 版本走 |
| Compose 库 | AndroidX BOM 2025.04.01 | Compose Multiplatform 1.11.x（`org.jetbrains.compose` 插件） | commonMain 统一使用 `org.jetbrains.compose.*` 坐标；Android target 由 Gradle 元数据自动重定向到 AndroidX 实现 |
| AGP | 8.6.0 | 8.9+（与 Kotlin 2.2 兼容的最新 8.x） | 实现时以官方兼容矩阵为准 |
| JDK | 17 | 17 | 不变 |
| 发布插件 | vanniktech 0.34.0 | 移除，改用 Gradle 内置 `maven-publish` | GitHub Packages 不强制签名/javadoc/sources jar；KMP 的 `maven-publish` 自动为每个 target 生成 publication |
| Dokka | 1.9.20 | 保留并升级至匹配 Kotlin 2.2 的版本 | 继续服务文档站（docs.yml） |
| Kottie 等 | - | 不采用 | 采用 Compottie，见 §6 |

## 5. 模块结构与目标平台

### 5.1 目录结构（以 `refresh` 为例，4 个库模块一致）

```text
refresh/
  build.gradle.kts                # kotlin-multiplatform + compose + maven-publish
  src/
    commonMain/kotlin/            # 现有 src/main/kotlin 全部代码平移（除振动实现）
    androidMain/kotlin/           # 振动 actual（Android 实现）
    iosMain/kotlin/               # 振动 actual（UIKit 触觉反馈）
    sharedMain/kotlin/            # 自定义中间源集：macOS/JVM/JS/Wasm 共用的 no-op 振动 actual
```

- `src/main/res` 下的 `strings.xml` 为空文件，直接删除，无资源迁移负担
- 删除 `ExampleInstrumentedTest`/`ExampleUnitTest` 样板测试（无实质断言）
- `consumer-rules.pro`/`proguard-rules.pro`：保留在 androidMain 相关配置中（AGP KMP 模式下 androidTarget 仍支持 consumerProguardFiles）

### 5.2 Targets（4 个库模块统一）

```kotlin
androidTarget { publishLibraryVariants("release") }
jvm()
iosX64(); iosArm64(); iosSimulatorArm64()
macosX64(); macosArm64()
js(IR) { browser() }
wasmJs { browser() }
```

源集层级：使用 Kotlin 默认 hierarchy template，并自定义 `sharedMain` 中间源集（dependsOn jvmMain/jsMain/wasmJsMain/macosMain）存放 no-op 振动实现，避免 4 处重复代码。

## 6. 平台差异点：振动反馈（expect/actual）

commonMain 定义 internal 抽象（不进公共 API）：

```kotlin
// commonMain
internal expect fun createVibratorController(): VibratorController
internal interface VibratorController {
    fun vibrate(millis: Long)
    fun cancel()
}
```

| 平台 | 实现 |
|---|---|
| Android | 平移现有 `Vibrator`/`VibratorManager`/`VibrationEffect` 逻辑（API 26+ 走 `VibratorManager`，否则 `Vibrator`） |
| iOS | `UIKit.UIImpactFeedbackGenerator`（`iosMain`） |
| macOS/JVM/JS/Wasm | no-op（`sharedMain`） |

`UltraSwipeRefresh.kt` 中 `LocalContext` 相关代码随振动实现一并下沉到 androidMain；`androidx.annotation.FloatRange/IntRange` 注解直接删除（纯文档性质，不影响运行时与 API 行为）。

## 7. Lottie 指示器：Compottie 替换 lottie-compose

- 依赖：`io.github.alexzhirkevich:compottie:2.2.x`（当前最新 2.2.4；MIT；自研纯多平台渲染引擎；支持 Android JVM/Desktop JVM/iOS/macOS/JS/Wasm；兼容 CMP 1.10–1.12）——来源：klibs.io/project/alexzhirkevich/compottie
- 采用 **compottie 主模块**（保守保留 AE 表达式兼容；如后续在意包体积可一行切换 `compottie-lite`）
- API 迁移：`com.airbnb.lottie.compose.*` → `io.github.alexzhirkevich.compottie.*`。现有 `LottieRefreshIndicator` 使用的 `rememberLottieComposition`/`animateLottieCompositionAsState`/`LottieAnimation`/`LottieConstants` 与 Compottie API 几乎 1:1，以包名替换为主
- **Breaking change（可接受，随 2.0.0 发布）**：
  - `LottieCompositionSpec.RawRes`/`Asset` 为 Android 资源概念，Compottie 无对应项；本模块公共 API 中的 spec 类型随 Compottie 变化
  - CHANGELOG 提供迁移说明：动画 JSON 建议改放 `commonMain/composeResources`，以 `LottieCompositionSpec.JsonString` 方式加载
- artifactId `refresh-indicator-lottie` 不变，Compottie 成为传递依赖

## 8. 发布：GitHub Packages

### 8.1 发布配置

- 仓库：`https://maven.pkg.github.com/jenly1314/UltraSwipeRefresh`
- 凭证：`gradle.properties`（`gpr.user` / `gpr.key`，或环境变量 `GITHUB_ACTOR` / `GITHUB_TOKEN`）
- group 保持 `com.github.jenly1314.UltraSwipeRefresh`；POM 元数据沿用现有 `gradle.properties` 的 `POM_*` 属性，由根构建脚本统一下发到各模块 publication
- 版本号：`VERSION_NAME=2.0.0`
- KMP `maven-publish` 自动产出：每平台 artifact（`-android`/`-jvm`/`-iosarm64`/…）+ 根 metadata artifact（`refresh` 无后缀，携带 Gradle Module Metadata 供消费者按平台解析）

### 8.2 CI 调整

- `publish.yml` / `release.yml`：改为 `permissions: packages: write`，使用 `GITHUB_TOKEN` 执行 `./gradlew publish`；移除 Maven Central（MAVEN_CENTRAL_USERNAME/PASSWORD）与 GPG 相关 secrets
- `build.yml`：保持；Kotlin/Native 可在 Linux runner 交叉编译 iOS/macOS target，无需 macOS 机器
- 保留 `jitpack.yml`：作为匿名下载的补充渠道（GitHub Packages 下载需认证）

### 8.3 已知限制（需写入 README）

- GitHub Packages 的 Maven 仓库即使公开包也需认证下载：消费者须配置 `read:packages` PAT
- 消费者需在 `repositories` 中添加 `maven("https://maven.pkg.github.com/jenly1314/UltraSwipeRefresh")`

## 9. demo KMP 化

- `app` → `composeApp` 模块：`kotlin-multiplatform` + `compose` 插件
- Android target：保留现有 demo 全部功能与代码（含 accompanist-swiperefresh 对比演示，仅存在于 androidMain）
- 其他平台：新增 iOS（`MainViewController.kt`）、Desktop（`main.kt`）、JS/Wasm（浏览器入口），共享同一套 demo Compose 界面（`commonMain`）
- demo 不发布（无 maven-publish）

## 10. 验证策略

1. `./gradlew build`：全部 targets 编译通过
2. `./gradlew publishToMavenLocal`：检查产物结构（各平台 artifact + 根 metadata + POM 正确）
3. `composeApp` 在 Android 与 Desktop JVM 实机冒烟（iOS/Web 本地无环境，依赖 CI 编译 + 后续人工验证）
4. 4 个库模块 `publishAllPublicationsToLocalMaven` 后，用一个临时 KMP 消费工程验证 GitHub Packages 坐标解析（本地仓库模拟）
5. README / CHANGELOG 同步更新：平台支持矩阵、新仓库坐标与认证说明、lottie 模块迁移说明

## 11. 风险与对策

| 风险 | 对策 |
|---|---|
| GitHub Packages 下载需认证，影响开源分发 | README 显著位置说明；保留 jitpack 渠道 |
| Kotlin 2.x 编译更严格，存量代码可能有告警/小报错 | 迁移时逐个修复；均为低风险 |
| CMP 1.11 与旧 AndroidX Compose API 差异 | 现有用到的 nestedScroll/layout API 均为稳定 API；编译期即可暴露问题 |
| JS/Wasm canvas 渲染性能差异 | 非阻塞；README 标注性能特征 |
| Compottie spec 类型 breaking | §7 已定义迁移说明，随 2.0.0 CHANGELOG 发布 |

## 12. 关键决策记录

1. **全平台 targets**（用户选择）：Android + iOS + Desktop JVM + macOS + JS + Wasm
2. **方案 A：原地 KMP 转换 + 原生 maven-publish + 直接配置**（用户选择，否决 convention plugins 方案 B 与核心先行方案 C）
3. **Lottie 用 Compottie 主模块**（用户指定 klibs.io/project/alexzhirkevich/compottie；主模块而非 lite，保留表达式兼容）
4. **发布仅 GitHub Packages**（用户选择；放弃 Maven Central 链路，保留 jitpack 补充渠道）
5. **demo 同步 KMP 化**（用户选择）
6. **版本号 2.0.0**：Kotlin 大版本升级 + KMP 产物结构 + lottie 模块公共 API 变化
