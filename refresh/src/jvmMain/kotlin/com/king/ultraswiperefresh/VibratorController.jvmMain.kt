package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable

/**
 * 不支持振动的平台的空实现
 */
@Composable
internal actual fun rememberVibratorController(): VibratorController? = null
