package com.king.ultraswiperefresh.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        title = "UltraSwipeRefresh Demo",
        state = rememberWindowState(),
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
