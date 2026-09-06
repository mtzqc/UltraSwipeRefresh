package com.king.ultraswiperefresh.app.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.king.ultraswiperefresh.NestedScrollMode
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.app.DemoScreen
import com.king.ultraswiperefresh.app.component.ColumnItem
import com.king.ultraswiperefresh.app.ext.rememberToast
import com.king.ultraswiperefresh.app.generated.resources.Res
import com.king.ultraswiperefresh.app.generated.resources.intro_content
import com.king.ultraswiperefresh.app.generated.resources.intro_title
import com.king.ultraswiperefresh.app.generated.resources.random_mode_content
import com.king.ultraswiperefresh.app.generated.resources.random_mode_title
import com.king.ultraswiperefresh.app.generated.resources.toast_random_mode
import com.king.ultraswiperefresh.app.generated.resources.toast_vibration_disabled
import com.king.ultraswiperefresh.app.generated.resources.toast_vibration_enabled
import com.king.ultraswiperefresh.indicator.SwipeRefreshFooter
import com.king.ultraswiperefresh.indicator.SwipeRefreshHeader
import com.king.ultraswiperefresh.theme.UltraSwipeRefreshTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * UltraSwipeRefresh 示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun UltraSwipeRefreshSample(screens: List<DemoScreen>, openScreen: (String) -> Unit) {

    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var headerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }
    var footerScrollMode by remember {
        mutableStateOf(NestedScrollMode.FixedContent)
    }

    val toast = rememberToast()
    val toastVibrationEnabled = stringResource(Res.string.toast_vibration_enabled)
    val toastVibrationDisabled = stringResource(Res.string.toast_vibration_disabled)
    val toastRandomMode = stringResource(Res.string.toast_random_mode)

    UltraSwipeRefresh(
        isRefreshing = isRefreshing,
        isLoading = isLoading,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                // TODO 刷新的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                isRefreshing = false
            }
        },
        onLoadMore = {
            coroutineScope.launch {
                isLoading = true
                // TODO 加载更多的逻辑处理，此处的延时只是为了演示效果
                delay(2000)
                isLoading = false
            }
        },
        modifier = Modifier.background(color = Color(0x7FEEEEEE)),
        headerScrollMode = headerScrollMode,
        footerScrollMode = footerScrollMode,
        headerIndicator = {
            SwipeRefreshHeader(it)
        },
        footerIndicator = {
            SwipeRefreshFooter(it)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {

            item {
                ColumnItem(
                    title = stringResource(Res.string.intro_title),
                    content = stringResource(Res.string.intro_content)
                ) {
                    val vibrateEnabled = !UltraSwipeRefreshTheme.config.vibrationEnabled
                    UltraSwipeRefreshTheme.config =
                        UltraSwipeRefreshTheme.config.copy(vibrationEnabled = vibrateEnabled)
                    if (vibrateEnabled) {
                        toast(toastVibrationEnabled)
                    } else {
                        toast(toastVibrationDisabled)
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }

            item {
                val nestedScrollModes = remember { NestedScrollMode.entries }
                ColumnItem(
                    title = stringResource(Res.string.random_mode_title),
                    content = stringResource(
                        Res.string.random_mode_content,
                        headerScrollMode.name,
                        footerScrollMode.name
                    )
                ) {
                    headerScrollMode = nestedScrollModes.random()
                    footerScrollMode = nestedScrollModes.random()
                    toast(toastRandomMode)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF2F3F6)
                )
            }

            screens.forEach { screen ->
                item {
                    ColumnItem(title = screen.title, content = screen.content) {
                        openScreen(screen.name)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF2F3F6)
                    )
                }
            }
        }
    }
}
