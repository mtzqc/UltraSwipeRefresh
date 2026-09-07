## 版本日志

#### v2.0.0 ：2026-9-5
- **重大变更：项目全面迁移至 Kotlin Multiplatform**，支持平台：Android、iOS（arm64/Simulator）、Desktop（Windows/macOS/Linux）、macOS（arm64）、JS、Wasm
- 更新Kotlin至v2.3.20 (v1.9.20 -> v2.3.20)；Compose改用Compose Multiplatform v1.11.1
- minSdk变更为24（v21 -> v24，由Compottie依赖要求）
- 现有 Maven Central 上的 v1.x 产物仍可继续使用，但不再更新
- **发布渠道说明**：2.x 起继续通过 Maven Central（Central Portal）发布，消费方式不变；KMP 产物不支持 JitPack，移除 jitpack 渠道相关配置
- `refresh-indicator-lottie`：lottie-compose替换为[Compottie](https://github.com/alexzhirkevich/compottie) v2.2.4（多平台）；公共API中`LottieCompositionSpec`类型随Compottie变化，`RawRes`/`Asset`不再可用——动画JSON建议放置于`commonMain/composeResources/files`后自行读取内容并以`LottieCompositionSpec.JsonString`构造（可参考库内`DefaultLottieSpec`实现）
- 振动反馈改为跨平台expect/actual：Android沿用Vibrator、iOS使用UIKit触觉反馈、Desktop/JS/Wasm为no-op
- 移除`androidx.annotation.FloatRange/IntRange`注解（公共API签名不受影响）
- demo模块KMP化为`composeApp`（Android/Desktop/iOS/JS/Wasm多入口）

#### v1.6.0 ：2026-8-4
- 更新compileSdk至35
- 更新compose至v1.8.0 (v1.7.0 -> v1.8.0)
- 更新Gradle至v8.9
- 源码目录调整（src/main/java -> src/main/kotlin）

#### v1.5.0 ：2026-3-28
- 新增：支持Header/Footer二级内容，提供完整的配置参数
- 新增参数`headerSecondaryContent` / `footerSecondaryContent`：Header/Footer二级内容
- 新增参数`headerSecondaryEnabled` / `footerSecondaryEnabled`：是否启用Header/Footer二级内容功能
- 新增参数`headerSecondaryBehavior` / `footerSecondaryBehavior`：Header/Footer二级内容交互行为模式
- 新增参数`headerSecondaryPreview` / `footerSecondaryPreview`：Header/Footer二级内容是否可提前预览
- 新增参数`headerSecondaryTriggerRate` / `footerSecondaryTriggerRate`：触发Header/Footer二级的最小滑动比例
- 优化：为新增的 Header/Footer 二级内容功能进行整体适配与交互优化

#### v1.4.2 ：2025-9-6
- 优化显示细节（[#38](https://github.com/jenly1314/UltraSwipeRefresh/issues/38)）

#### v1.4.1 ：2025-8-11
- 新增参数`onCollapseScroll`：可选回调，当Header/Footer收起时需要同步调整列表位置以消除视觉回弹时使用

#### v1.4.0 ：2025-7-21
- 迁移发布至 **Central Portal** [相关公告](https://central.sonatype.org/pages/ossrh-eol/#logging-in-to-central-portal)
- 更新compose至v1.7.0 (v1.6.0 -> v1.7.0)
- 更新lottie至v6.6.0 (v6.1.0 -> v6.6.0)
- 更新Gradle至v8.5
- 参数名变更：将原 `vibrateEnabled` 修改为：`vibrationEnabled`
- 新增参数`vibrationMillis`: 振动时长
- 优化一些细节

#### v1.3.1 ：2024-12-23
- 修复BUG：快速滑动时，出现收起动画不执行的问题。 （[#21](https://github.com/jenly1314/UltraSwipeRefresh/issues/21)）

#### v1.3.0 ：2024-7-20
- 更新compose至v1.6.0 (v1.5.0 -> v1.6.0) （[#13](https://github.com/jenly1314/UltraSwipeRefresh/issues/13)）
- 新增参数`alwaysScrollable`：是否始终可以滚动
- 优化一些细节

#### v1.2.0 ：2024-7-1
- 新增参数`contentContainer`：内容的父容器，便于统一管理
- 修复了一些已知问题

#### v1.1.3 ：2024-6-2
- 修复BUG：禁用下拉刷新或上拉加载时，上下滑动的边界值判定问题。（[#8](https://github.com/jenly1314/UltraSwipeRefresh/issues/8)）
- 优化一些细节

#### v1.1.2 ：2024-5-22
- 修复BUG：部分机型在某些特定场景下，出现“无法再次触发下拉刷新”的问题。（[#7](https://github.com/jenly1314/UltraSwipeRefresh/issues/7)）

#### v1.1.1 ：2024-4-20
- 修复BUG：刷新状态变化太快时，导致”完成时的定格提示动画”不执行的问题。（[#4](https://github.com/jenly1314/UltraSwipeRefresh/issues/4)）
- 优化一些细节

#### v1.1.0 ：2023-12-17
- 新增`UltraSwipeRefreshTheme`：用于统一管理全局默认配置
- 新增参数`finishDelayMillis`：完成时延时时间（可用于定格展示提示内容）
- 新增参数`vibrateEnabled`：是否启用振动（当滑动偏移量满足触发刷新或触发加载更多时，会有振动效果）

#### v1.0.0 ：2023-12-3
- UltraSwipeRefresh初始版本
