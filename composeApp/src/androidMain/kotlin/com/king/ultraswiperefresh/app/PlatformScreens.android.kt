package com.king.ultraswiperefresh.app

import com.king.ultraswiperefresh.app.sample.SwipeRefreshSample

/**
 * Android 专属示例：Accompanist SwipeRefresh 对比演示
 */
internal actual fun platformExtraScreens(): List<DemoScreen> = listOf(
    DemoScreen(
        name = "SwipeRefreshSample",
        title = "Accompanist中的SwipeRefresh示例",
        content = "只支持下拉刷新，此示例主要用于与UltraSwipeRefresh进行效果对比（后续可能会移除）",
        screen = { SwipeRefreshSample() },
    ),
)
