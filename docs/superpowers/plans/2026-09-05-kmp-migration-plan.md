# UltraSwipeRefresh KMP 迁移实现计划

- 日期：2026-09-05
- 设计文档：`docs/superpowers/specs/2026-09-05-kmp-migration-design.md`（已确认）
- 分支：`feature/kmp-migration`（不得直接在 master 上实现）
- 执行方式：逐任务执行，每个任务完成后运行其验证命令，绿了才进下一个任务

## 全局约束

- 4 个库模块 artifactId 不变：`refresh`、`refresh-indicator-classic`、`refresh-indicator-progress`、`refresh-indicator-lottie`；group 不变 `com.github.jenly1314.UltraSwipeRefresh`；版本 `2.0.0`
- 库模块 targets 统一：android / jvm / iosX64 / iosArm64 / iosSimulatorArm64 / macosX64 / macosArm64 / js(IR, browser) / wasmJs(browser)
- commonMain 一律使用 `org.jetbrains.compose.*` 坐标（`compose.foundation`、`compose.ui` 等 DSL），不引用 AndroidX Compose 坐标
- 公共 API 签名除 §5 的 lottie spec 类型外全部保持不变
- 环境为 Windows：库模块的 iOS/macOS target 只需编译 klib（Kotlin/Native 交叉编译，无需 Apple 工具链）；不做 iOS 壳工程（Xcode project 不在范围内）

## Task 0：分支与基线

1. `git checkout -b feature/kmp-migration`
2. 基线编译：`./gradlew :refresh:assembleRelease :app:assembleDebug`
3. 记录结果（若基线就失败，停下询问用户）

## Task 1：版本底座升级（保持 Android-only 结构先变绿）

1. `gradle/wrapper/gradle-wrapper.properties`：distributionUrl 升到 8.x 最新（≥8.14）
2. `gradle/libs.versions.toml` 更新：
   - `kotlin = "2.2.21"`、`dokka = "2.0.0"`（以与 Kotlin 2.2 兼容的最新稳定版为准）
   - `agp` 升到与 Kotlin 2.2 / 所选 Gradle 兼容的最新 8.x（实现时查官方兼容矩阵确定 patch 号）
   - 新增 `composeMultiplatform = "1.11.x"`（与 Kotlin 2.2.21 兼容的最新稳定版）及插件 `compose-multiplatform = { id = "org.jetbrains.compose", version.ref = ... }`
   - 新增 `kotlin-compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }`
   - 新增 `compottie = "2.2.4"`、库 `compottie = { module = "io.github.alexzhirkevich:compottie", version.ref = ... }`
   - 删除 `composeCompiler`、`mavenPublish`、`airbnb-lottie-compose` 相关条目（lottie 条目在 Task 5 前保留给 app？否——app 不用 lottie 库本身，直接删）
3. 根 `build.gradle.kts`：plugins 块按新插件调整，移除 vanniktech
4. 4 个 Android 库模块 + app 的 build 脚本：`kotlinOptions`/`composeOptions` 块删除，插件列表加 `alias(libs.plugins.kotlin.compose.compiler)`；`sourceCompatibility`/`jvmTarget` 升到 17
5. 验证：
   - `./gradlew :refresh:assembleRelease :app:assembleDebug` 通过
   - `./gradlew --version` 显示新 wrapper 版本

## Task 2：refresh 核心 KMP 化

1. `git mv refresh/src/main/kotlin refresh/src/commonMain/kotlin`
2. 删除：`refresh/src/main/res`（空 strings.xml）、`refresh/src/test` 与 `refresh/src/androidTest` 样板测试
3. 振动 expect/actual 改造：
   - commonMain 新增 `com/king/ultraswiperefresh/VibratorController.kt`：
     ```kotlin
     internal interface VibratorController { fun hasVibrator(): Boolean; fun vibrate(millis: Long) }
     @Composable internal expect fun rememberVibratorController(): VibratorController?
     ```
   - `UltraSwipeRefresh.kt`：`VibrationLaunchedEffect` 改用 `rememberVibratorController()`（null 或 `hasVibrator()==false` 直接 return；`LaunchedEffect` 内仅调 `vibrate(vibrationMillis)`）；删除文件内所有 android/`LocalContext` import 与 `rememberVibrator()`
   - androidMain 新增 actual：平移原 `rememberVibrator()` 逻辑（API 31+ `VibratorManager`，否则 `Vibrator`），`vibrate()` 内部处理 API 26+ `VibrationEffect.createOneShot`
   - iosMain actual：`UIKit.UIImpactFeedbackGenerator`，`vibrate` 调 `impactOccurred()`
   - `sharedMain`（自定义中间源集，dependsOn jvm/js/wasmJs/macosMain）actual：返回 null
   - 删除 `androidx.annotation.FloatRange/IntRange` 注解及 import（3 个文件：UltraSwipeRefresh.kt、theme/UltraSwipeRefreshTheme.kt、indicator/ProgressIndicator.kt）
4. `refresh/build.gradle.kts` 重写为 KMP：`kotlin-multiplatform` + `compose` + `dokka` 插件；§"全局约束"的 9 个 targets；`androidTarget { publishLibraryVariants("release"); ... consumerProguardFiles }`；依赖 `compose.foundation`；测试源集暂不配
5. 验证：
   - `./gradlew :refresh:build` 全 targets 通过
   - `./gradlew :refresh:publishToMavenLocal`，检查 `~/.m2/repository/com/github/jenly1314/UltraSwipeRefresh/refresh/` 出现各平台后缀 artifact + 无后缀根 metadata

## Task 3：refresh-indicator-progress KMP 化

复制 Task 2 的模式（无振动、无额外依赖，纯 Compose）：
1. `git mv` 源码到 commonMain；删样板测试
2. build 脚本 KMP 化（同 Task 2 第 4 步，另加 `compileOnly(project(":refresh"))` → 改为 KMP 下 `commonMainApi(project(":refresh"))`——indicator 与 core 同包且引用其 API）
3. 验证：`./gradlew :refresh-indicator-progress:build`

## Task 4：refresh-indicator-classic KMP 化

同 Task 3 模式。
1. 验证：`./gradlew :refresh-indicator-classic:build`

## Task 5：refresh-indicator-lottie KMP 化 + Compottie

1. 源码迁移同上；imports `com.airbnb.lottie.compose.*` → `io.github.alexzhirkevich.compottie.*`（类名逐一核对 Compottie 2.2.4 的 API：`LottieAnimation`、`rememberLottieComposition`、`animateLottieCompositionAsState`、`LottieConstants`、`LottieCompositionSpec`、`LottieCancellationBehavior`，以 klibs.io/README 为准）
2. build 脚本：`commonMainApi(libs.compottie)` + `commonMainApi(project(":refresh"))`（原 `api(lottie)` 语义保持为 api）
3. 公共 API 中 spec 类型随 Compottie 变化（Breaking，随 2.0.0 记录）；`RawRes`/`Asset` 无对应项
4. 验证：
   - `./gradlew :refresh-indicator-lottie:build`
   - `./gradlew :app:assembleDebug`（app 依赖 4 个模块，等价于一次集成编译）

## Task 6：发布配置（GitHub Packages）

1. 根 `build.gradle.kts` 增加共享发布配置（`subprojects { plugins.withId("org.gradle.maven-publish") { ... } }`）：
   - 仓库 `maven { name="GitHubPackages"; url = uri("https://maven.pkg.github.com/jenly1314/UltraSwipeRefresh"); credentials { username = gpr.user|GITHUB_ACTOR; password = gpr.key|GITHUB_TOKEN } }`
   - 每个 `MavenPublication`：groupId/group=GROUP、version=VERSION_NAME（gradle.properties）、artifactId=模块 `POM_ARTIFACT_ID`、POM 填充现有 `POM_*` 属性（name/description/url/scm/licenses/developers）
2. 各库模块 `gradle.properties` 的 `POM_NAME/POM_ARTIFACT_ID` 保持不变
3. 验证：
   - `./gradlew publishToMavenLocal` 全模块通过
   - `./gradlew :refresh:tasks --all | grep -i publish` 能看到 `publishAllPublicationsToGitHubPackagesRepository`
   - 抽查 mavenLocal 中任一 `.module` 文件包含全部 targets 的 variant

## Task 7：CI 调整

1. `.github/workflows/publish.yml` 与 `release.yml` 重写：去掉 jenly1314/actions 的 reusable-publish（其面向 Maven Central+GPG），改为内联 job——JDK 17 + `gradle/actions/setup-gradle` + `./gradlew publish`，`permissions: { packages: write, contents: read }`，secrets 传 `GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}`、`GITHUB_ACTOR: ${{ github.actor }}`；release.yml 保留发 GitHub Release 的既有逻辑
2. `build.yml` 不动（仍用 reusable-build）
3. 验证：`actionlint`（若可用）或逐行人工检查 YAML；不真实触发发布

## Task 8：demo KMP 化（app → composeApp）

1. `git mv app composeApp`
2. `composeApp/build.gradle.kts` 重写：`kotlin-multiplatform` + `compose` + `android.application`（KMP 内 androidTarget 配置沿用现 applicationId/minSdk 等）
3. 源集结构：
   - commonMain：demo UI 主体（各 Sample 屏 + DemoApp 根组件）从现 `app/src/main/java` 迁入；navigation 用 `org.jetbrains.androidx.navigation:navigation-compose`（版本与 CMP 1.11 兼容的最新稳定版；**若发现不兼容，降级方案：demo 改单屏 + 滚动切换示例，去掉 navigation 依赖**——此决策留给执行者，遇到即采用降级方案并记录）
   - androidMain：`MainActivity`、accompanist-swiperefresh 对比演示（Android-only 依赖）、manifest
   - jvmMain：`main.kt`（`Window` + DemoApp）
   - iosMain：`MainViewController.kt`（返回 DemoApp 的 ViewController）
   - jsMain/wasmJsMain：浏览器入口 + `index.html`
4. settings.gradle.kts：`:app` → `:composeApp`
5. 验证：
   - `./gradlew :composeApp:assembleDebug`（Android）
   - `./gradlew :composeApp:packageReleaseDistributionForCurrentOS`（Desktop；或对应 package 任务）
   - `./gradlew :composeApp:wasmJsBrowserDistribution` 与 `:composeApp:jsBrowserDistribution` 编译通过
   - 本机若有模拟器/设备：`installDebug` 冒烟（可选）

## Task 9：文档与版本号

1. `gradle.properties`：`VERSION_NAME=2.0.0`、`VERSION_CODE=14`、`POM_DESCRIPTION` 改为覆盖多平台的描述
2. README：平台支持矩阵、GitHub Packages 仓库配置示例（含 `read:packages` PAT 认证说明）、依赖坐标（2.0.0）、lottie 模块迁移说明（`RawRes`/`Asset` → composeResources + `JsonString`）
3. CHANGELOG.md：新增 2.0.0 条目（KMP 全平台、GitHub Packages、Compottie breaking、Kotlin/CMP 版本要求）
4. 验证：文档内坐标与仓库 URL 逐一比对；`git diff` 自查

## Task 10：总验证与收尾

1. `./gradlew build`（全模块全 targets）
2. `./gradlew publishToMavenLocal` 全模块通过
3. 清理：确认无遗留 `src/main/kotlin` 结构、无 vanniktech 引用、`./gradlew :refresh:dependencies --configuration releaseRuntimeClasspath` 无 lottie-android 残留
4. `git status` 干净，提交历史按任务分 commit
5. 汇报：变更摘要 + 用户手动验证建议（Android/桌面运行 demo；配 PAT 后真实 publish 验证）

## 已知风险与执行者止损点

- 遇到 AGP/Gradle/Kotlin patch 兼容问题：以官方兼容矩阵为准调整 patch 版本，不改变大版本方向
- 遇到 Compottie API 与计划不符：以 Compottie 2.2.x 文档为准，保持"API 迁移以包名替换为主"的原则
- 遇到 JetBrains navigation 不兼容：按 Task 8 第 3 步的降级方案执行
- 任一任务验证两次失败：停下向用户报告，不猜测绕过
