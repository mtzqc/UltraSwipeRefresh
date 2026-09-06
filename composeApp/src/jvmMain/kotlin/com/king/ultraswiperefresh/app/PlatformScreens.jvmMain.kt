package com.king.ultraswiperefresh.app

import androidx.compose.runtime.Composable

/**
 * 非 Android 平台无平台差异示例
 */
@Composable
internal actual fun platformExtraScreens(): List<DemoScreen> = emptyList()
