package com.king.ultraswiperefresh.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.app.generated.resources.Res
import com.king.ultraswiperefresh.app.generated.resources.back
import com.king.ultraswiperefresh.app.generated.resources.screen_classic_auto_load_content
import com.king.ultraswiperefresh.app.generated.resources.screen_classic_auto_load_title
import com.king.ultraswiperefresh.app.generated.resources.screen_classic_refresh_indicator_content
import com.king.ultraswiperefresh.app.generated.resources.screen_classic_refresh_indicator_title
import com.king.ultraswiperefresh.app.generated.resources.screen_custom_lottie_content
import com.king.ultraswiperefresh.app.generated.resources.screen_custom_lottie_title
import com.king.ultraswiperefresh.app.generated.resources.screen_lottie_refresh_indicator_content
import com.king.ultraswiperefresh.app.generated.resources.screen_lottie_refresh_indicator_title
import com.king.ultraswiperefresh.app.generated.resources.screen_progress_refresh_indicator_content
import com.king.ultraswiperefresh.app.generated.resources.screen_progress_refresh_indicator_title
import com.king.ultraswiperefresh.app.generated.resources.screen_pull_refresh_content
import com.king.ultraswiperefresh.app.generated.resources.screen_pull_refresh_title
import com.king.ultraswiperefresh.app.generated.resources.screen_secondary_content_content
import com.king.ultraswiperefresh.app.generated.resources.screen_secondary_content_title
import com.king.ultraswiperefresh.app.generated.resources.screen_swipe_refresh_indicator_content
import com.king.ultraswiperefresh.app.generated.resources.screen_swipe_refresh_indicator_title
import com.king.ultraswiperefresh.app.sample.ClassicRefreshAutoLoadSample
import com.king.ultraswiperefresh.app.sample.ClassicRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.CustomLottieRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.LottieRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.ProgressRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.PullRefreshSample
import com.king.ultraswiperefresh.app.sample.SecondaryContentSample
import com.king.ultraswiperefresh.app.sample.SwipeRefreshIndicatorSample
import com.king.ultraswiperefresh.app.sample.UltraSwipeRefreshSample
import com.king.ultraswiperefresh.app.ui.theme.RefreshLayoutTheme
import org.jetbrains.compose.resources.stringResource

/**
 * 示例条目
 */
data class DemoScreen(
    val name: String,
    val title: String,
    val content: String,
    val screen: @Composable () -> Unit,
)

/**
 * UltraSwipeRefresh 示例入口（跨平台）
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun App() {
    RefreshLayoutTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            var currentName by remember { mutableStateOf<String?>(null) }
            val screens = ultraSwipeRefreshScreens() + platformExtraScreens()
            val current = screens.firstOrNull { it.name == currentName }
            if (current == null) {
                UltraSwipeRefreshSample(
                    screens = screens,
                    openScreen = { currentName = it },
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = { currentName = null }) {
                            Text(text = stringResource(Res.string.back))
                        }
                        Text(text = current.title, fontSize = 16.sp)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        current.screen()
                    }
                }
            }
        }
    }
}

@Composable
private fun ultraSwipeRefreshScreens(): List<DemoScreen> = listOf(
    DemoScreen(
        name = "SwipeRefreshIndicatorSample",
        title = stringResource(Res.string.screen_swipe_refresh_indicator_title),
        content = stringResource(Res.string.screen_swipe_refresh_indicator_content),
        screen = { SwipeRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "ClassicRefreshIndicatorSample",
        title = stringResource(Res.string.screen_classic_refresh_indicator_title),
        content = stringResource(Res.string.screen_classic_refresh_indicator_content),
        screen = { ClassicRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "ClassicRefreshAutoLoadSample",
        title = stringResource(Res.string.screen_classic_auto_load_title),
        content = stringResource(Res.string.screen_classic_auto_load_content),
        screen = { ClassicRefreshAutoLoadSample() },
    ),
    DemoScreen(
        name = "ProgressRefreshIndicatorSample",
        title = stringResource(Res.string.screen_progress_refresh_indicator_title),
        content = stringResource(Res.string.screen_progress_refresh_indicator_content),
        screen = { ProgressRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "LottieRefreshIndicatorSample",
        title = stringResource(Res.string.screen_lottie_refresh_indicator_title),
        content = stringResource(Res.string.screen_lottie_refresh_indicator_content),
        screen = { LottieRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "CustomLottieRefreshIndicatorSample",
        title = stringResource(Res.string.screen_custom_lottie_title),
        content = stringResource(Res.string.screen_custom_lottie_content),
        screen = { CustomLottieRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "SecondaryContentSample",
        title = stringResource(Res.string.screen_secondary_content_title),
        content = stringResource(Res.string.screen_secondary_content_content),
        screen = { SecondaryContentSample() },
    ),
    DemoScreen(
        name = "PullRefreshSample",
        title = stringResource(Res.string.screen_pull_refresh_title),
        content = stringResource(Res.string.screen_pull_refresh_content),
        screen = { PullRefreshSample() },
    ),
)
