# 分发信息修复 + UltraSwipeRefresh.kt 拆分 实现计划

- 日期：2026-09-06
- 设计：`docs/superpowers/specs/2026-09-06-distribution-and-core-split-design.md`
- 分支：`feature/kmp-migration`（延续）

## Task 1：分发信息修复（纯文档，独立提交）

1. `README.md`：
   - APK 徽章与正文下载链接 `app/release/app-release.apk` → `composeApp/release/app-release.apk`
   - 「引入」部分 GitHub Packages 之后新增「JitPack 备选渠道」小节（匿名、坐标相同、可用性以 JitPack 构建为准），并补充渠道说明（Central 仅 1.x 遗留）
2. `jitpack.yml`：补充 KMP 构建注释（保持 openjdk17 不变）
3. `CHANGELOG.md`：v2.0.0 条目补充 JitPack 备选渠道说明

验证：链接路径/文本逐项比对（无构建需求）

## Task 2：UltraSwipeRefresh.kt 拆分（独立提交）

新建 3 个文件（同包 `com.king.ultraswiperefresh`，代码逐行平移，private → internal）：

1. `UltraSwipeRefreshLayout.kt`：`obtainHeaderOffset` / `obtainContentOffset` / `obtainFooterOffset` / `obtainZIndex` / `RefreshSubComposeLayout`
2. `SecondaryContent.kt`：`HeaderSecondaryContent` / `FooterSecondaryContent`
3. `UltraSwipeVibration.kt`：`VibrationLaunchedEffect`

改写 `UltraSwipeRefresh.kt`：只保留两个公共重载 + KDoc，import 精简。

验证：
- `./gradlew :refresh:assemble` 全 targets
- 移动代码块与原文逐行一致（仅可见性变化）
