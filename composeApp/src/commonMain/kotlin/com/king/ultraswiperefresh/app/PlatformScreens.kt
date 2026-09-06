package com.king.ultraswiperefresh.app

import androidx.compose.runtime.Composable

/**
 * 平台差异示例（如 Android 专属的 Accompanist 对比示例）
 */
@Composable
internal expect fun platformExtraScreens(): List<DemoScreen>
