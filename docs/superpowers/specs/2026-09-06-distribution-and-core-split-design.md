# 分发信息修复 + UltraSwipeRefresh.kt 拆分 设计

- 日期：2026-09-06
- 状态：已确认（用户 Ok）
- 前置：UltraSwipeRefresh v2.0.0 KMP 迁移已完成（`feature/kmp-migration` 分支）

## 1. 目标

1. 修复 README 中自相矛盾/失效的分发信息，确立「GitHub Packages 为主 + JitPack 为匿名备选」的双渠道策略
2. 将 644 行的 `UltraSwipeRefresh.kt` 拆分为职责单一的小文件，不改任何公共 API

## 2. 非目标

- 不发布新版本、不改版本号（VERSION_NAME 仍为 2.0.0）
- 不补测试、不动其他大文件（State/NestedScrollConnection 等）
- 不新增依赖

## 3. 分发信息修复

### 3.1 README.md

- 徽章区：
  - Maven Central 徽章保留并标注为 1.x 遗留渠道（链接不变，v1.x 产物仍在 Central）
  - JitPack 徽章保留
  - APK 下载徽章与正文链接：`app/release/app-release.apk` → `composeApp/release/app-release.apk`（目录已随 demo KMP 化改名）
- 「引入」部分新增「JitPack 备选渠道」小节：
  - 仓库地址 `https://jitpack.io`，依赖坐标与 GitHub Packages 完全相同
  - 如实注明：产物由 JitPack 在其环境构建，可用性以 JitPack 页面实际构建状态为准
  - 定位：为无法配置 GitHub PAT 的消费者提供匿名下载通道

### 3.2 jitpack.yml

- 保持 `jdk: openjdk17`（Kotlin 2.3.20 / AGP 8.13 均满足）
- 补充注释说明构建 KMP 多平台产物；Gradle wrapper 8.14.5 负责实际工具链
- 不引入自定义 install 命令等花哨配置

### 3.3 CHANGELOG.md

- v2.0.0 条目补充一句：新增 JitPack 备选下载渠道

### 3.4 验证与已知限制

- 本地可验证：README 内容正确性、jitpack.yml 语法
- 不可本地验证：JitPack 对 2.0.0 的真实构建（需推送 tag 后在 jitpack.io 触发）——**此验证属于用户操作**；若首次构建失败（内存/超时），依据构建日志调整后重试

## 4. UltraSwipeRefresh.kt 拆分

现状 644 行混合四类职责，拆分后公共 API 签名与行为零变化：

| 新文件 | 内容 | 来源行（约） | 可见性 |
|---|---|---|---|
| `UltraSwipeRefresh.kt` | 两个公共 `fun UltraSwipeRefresh` 重载 + 全部 KDoc + import 清理 | 1-413 | public |
| `UltraSwipeRefreshLayout.kt` | `RefreshSubComposeLayout` + `obtainHeaderOffset` / `obtainContentOffset` / `obtainFooterOffset` / `obtainZIndex` | 528-623 | private → internal |
| `SecondaryContent.kt` | `HeaderSecondaryContent` / `FooterSecondaryContent` | 415-522 | private → internal |
| `UltraSwipeVibration.kt` | `VibrationLaunchedEffect` | 625-末尾 | private → internal |

约束与验证：

- 纯代码移动 + 可见性放宽，禁止顺手修改任何逻辑
- 拆分后逐文件 diff 自查（移动的代码块与原文逐行一致，仅可见性/包内位置变化）
- 全 targets 编译验证（`:refresh:assemble`）
- 该文件无测试覆盖，以上述 diff 自查 + 编译作为回归保障

## 5. 提交策略

两部分各自独立提交（分发修复一笔、拆分一笔），便于回溯。

## 6. 决策记录

1. JitPack 采取「修复并保留」策略（用户未明确选择，按此前推荐执行）：匿名渠道对开源采用率价值大，且依赖坐标与 GitHub Packages 相同，切换零成本；README 如实标注可用性风险，推送 tag 后由用户验证，失败可低成本回退为移除
2. 拆分不引入 convention plugin / 不做其他大文件的重构（YAGNI）
