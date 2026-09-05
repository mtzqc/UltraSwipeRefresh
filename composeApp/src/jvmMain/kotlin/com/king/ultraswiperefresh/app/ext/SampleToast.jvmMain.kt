package com.king.ultraswiperefresh.app.ext

import androidx.compose.runtime.Composable

@Composable
internal actual fun rememberToast(): (String) -> Unit = { text ->
    println("[Toast] $text")
}
