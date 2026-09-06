package com.king.ultraswiperefresh.app

import androidx.compose.runtime.Composable
import com.king.ultraswiperefresh.app.generated.resources.Res
import com.king.ultraswiperefresh.app.generated.resources.screen_swipe_refresh_content
import com.king.ultraswiperefresh.app.generated.resources.screen_swipe_refresh_title
import com.king.ultraswiperefresh.app.sample.SwipeRefreshSample
import org.jetbrains.compose.resources.stringResource

/**
 * Android 专属示例：Accompanist SwipeRefresh 对比演示
 */
@Composable
internal actual fun platformExtraScreens(): List<DemoScreen> = listOf(
    DemoScreen(
        name = "SwipeRefreshSample",
        title = stringResource(Res.string.screen_swipe_refresh_title),
        content = stringResource(Res.string.screen_swipe_refresh_content),
        screen = { SwipeRefreshSample() },
    ),
)
