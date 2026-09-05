package com.king.ultraswiperefresh.app.ext

import androidx.compose.runtime.Composable

/**
 * 获取当前平台的 Toast 回调
 */
@Composable
internal expect fun rememberToast(): (String) -> Unit
