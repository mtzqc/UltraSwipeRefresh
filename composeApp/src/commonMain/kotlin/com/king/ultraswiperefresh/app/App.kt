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
            val screens = remember { ultraSwipeRefreshScreens() + platformExtraScreens() }
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
                            Text(text = "← Back")
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

private fun ultraSwipeRefreshScreens(): List<DemoScreen> = listOf(
    DemoScreen(
        name = "SwipeRefreshIndicatorSample",
        title = "默认刷新样式示例",
        content = "使用NestedScrollMode.FixedContent；特点：固定内容；即：内容固定，Header或 Footer进行滑动",
        screen = { SwipeRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "ClassicRefreshIndicatorSample",
        title = "经典刷新样式示例",
        content = "使用NestedScrollMode.Translate；特点：平移； 即：Header或 Footer与内容一起滑动",
        screen = { ClassicRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "ClassicRefreshAutoLoadSample",
        title = "经典刷新自动加载示例",
        content = "使用NestedScrollMode.Translate；特点：平移； 即：Header或 Footer与内容一起滑动，并可自动加载更多",
        screen = { ClassicRefreshAutoLoadSample() },
    ),
    DemoScreen(
        name = "ProgressRefreshIndicatorSample",
        title = "进度条刷新样式示例",
        content = "使用NestedScrollMode.FixedFront；特点：固定在前面；即：Header或 Footer和内容都固定，仅改变状态",
        screen = { ProgressRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "LottieRefreshIndicatorSample",
        title = "Lottie动画刷新样式示例",
        content = "使用NestedScrollMode.FixedBehind；特点：固定在背后；即：Header或 Footer固定，仅内容滑动",
        screen = { LottieRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "CustomLottieRefreshIndicatorSample",
        title = "自定义Lottie动画刷新样式示例",
        content = "随机切换滑动模式，Header与Footer与内容的联动效果由滑动模式[NestedScrollMode]来决定",
        screen = { CustomLottieRefreshIndicatorSample() },
    ),
    DemoScreen(
        name = "SecondaryContentSample",
        title = "二级内容示例",
        content = "下拉触发刷新阈值后可继续下拉进入Header二级内容（类似淘宝二楼），上拉触发加载阈值后可继续上拉进入Footer二级内容（地下室）示例",
        screen = { SecondaryContentSample() },
    ),
    DemoScreen(
        name = "PullRefreshSample",
        title = "Material中的Modifier.pullRefresh示例",
        content = "只支持下拉刷新，此示例主要用于与UltraSwipeRefresh进行效果对比（后续可能会移除）",
        screen = { PullRefreshSample() },
    ),
)
