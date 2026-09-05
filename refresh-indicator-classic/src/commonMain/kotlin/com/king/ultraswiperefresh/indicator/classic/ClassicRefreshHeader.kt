package com.king.ultraswiperefresh.indicator.classic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.ultraswiperefresh.indicator.classic.generated.resources.Res
import com.king.ultraswiperefresh.indicator.classic.generated.resources.*
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState

/**
 * 经典样式的指示器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
fun ClassicRefreshHeader(
    state: UltraSwipeRefreshState,
    modifier: Modifier = Modifier,
    tipContent: @Composable () -> String = {
        obtainHeaderTipContent(state)
    },
    tipTime: @Composable () -> String = {
        obtainLastRefreshTime(state)
    },
    tipContentStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 15.sp,
        color = Color(0xFF666666)
    ),
    tipTimeStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 12.sp,
        color = Color(0xFF999999)
    ),
    tipTimeVisible: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    arrowIconPainter: Painter = painterResource(Res.drawable.usr_classic_arrow),
    loadingIconPainter: Painter = painterResource(Res.drawable.usr_classic_spinner),
    tipMinWidth: Dp = 100.dp,
    iconSize: Dp = 24.dp,
    iconColorFilter: ColorFilter? = null,
) {
    ClassicRefreshIndicator(
        state = state,
        isFooter = false,
        tipContent = tipContent(),
        tipTime = tipTime(),
        modifier = modifier,
        tipContentStyle = tipContentStyle,
        tipTimeStyle = tipTimeStyle,
        tipTimeVisible = tipTimeVisible,
        paddingValues = paddingValues,
        arrowIconPainter = arrowIconPainter,
        loadingIconPainter = loadingIconPainter,
        tipMinWidth = tipMinWidth,
        iconSize = iconSize,
        iconColorFilter = iconColorFilter,
        label = "HeaderIndicator"
    )
}

/**
 * 根据[UltraSwipeRefreshState]获取提示内容
 */
@Composable
private fun obtainHeaderTipContent(state: UltraSwipeRefreshState): String {
    val textRes = when (state.headerState) {
        UltraSwipeHeaderState.PullDownToRefresh -> Res.string.usr_pull_down_to_refresh
        UltraSwipeHeaderState.ReleaseToRefresh -> Res.string.usr_release_to_refresh
        UltraSwipeHeaderState.Refreshing -> {
            if (state.isFinishing) {
                Res.string.usr_refresh_completed
            } else {
                Res.string.usr_refreshing
            }
        }
        UltraSwipeHeaderState.ReleaseToSecondary -> Res.string.usr_release_to_secondary_header
        UltraSwipeHeaderState.Secondary -> Res.string.usr_secondary_header
    }
    return stringResource(textRes)
}

/**
 * 获取上次刷新时间
 */
@Composable
private fun obtainLastRefreshTime(state: UltraSwipeRefreshState): String {
    var lastRefreshTime by remember {
        mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
    }
    LaunchedEffect(state.headerState) {
        if (state.headerState == UltraSwipeHeaderState.Refreshing) {
            lastRefreshTime = Clock.System.now().toEpochMilliseconds()
        }
    }
    val pattern = stringResource(Res.string.usr_time_format_pattern)
    val prefix = stringResource(Res.string.usr_last_refresh_time)
    return remember(lastRefreshTime, pattern) {
        val dateTime = Instant.fromEpochMilliseconds(lastRefreshTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        prefix + LocalDateTime.Format { byUnicodePattern(pattern) }.format(dateTime)
    }
}
