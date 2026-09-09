package com.king.ultraswiperefresh.indicator.classic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import com.king.ultraswiperefresh.UltraSwipeFooterState
import com.king.ultraswiperefresh.UltraSwipeHeaderState
import com.king.ultraswiperefresh.UltraSwipeRefreshState
import com.king.ultraswiperefresh.indicator.classic.generated.resources.Res
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_load_completed
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_last_load_time
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_last_refresh_time
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_loading
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_pull_down_to_refresh
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_pull_up_to_load
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_refresh_completed
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_refreshing
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_release_to_load
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_release_to_refresh
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_release_to_secondary_footer
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_release_to_secondary_header
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_secondary_footer
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_secondary_header
import com.king.ultraswiperefresh.indicator.classic.generated.resources.usr_time_format_pattern
import kotlinx.datetime.format.FormatStringsInDatetimeFormats

/**
 * 根据Header状态获取提示内容
 */
@Composable
internal fun obtainHeaderTipContent(state: UltraSwipeRefreshState): String {
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
 * 根据Footer状态获取提示内容
 */
@Composable
internal fun obtainFooterTipContent(state: UltraSwipeRefreshState): String {
    val textRes = when (state.footerState) {
        UltraSwipeFooterState.PullUpToLoad -> Res.string.usr_pull_up_to_load
        UltraSwipeFooterState.ReleaseToLoad -> Res.string.usr_release_to_load
        UltraSwipeFooterState.Loading -> {
            if (state.isFinishing) {
                Res.string.usr_load_completed
            } else {
                Res.string.usr_loading
            }
        }
        UltraSwipeFooterState.ReleaseToSecondary -> Res.string.usr_release_to_secondary_footer
        UltraSwipeFooterState.Secondary -> Res.string.usr_secondary_footer
    }
    return stringResource(textRes)
}

/**
 * 获取上次刷新/加载时间：进入刷新或加载状态时更新时间戳
 *
 * @param isActive 是否处于刷新/加载进行中
 * @param prefixRes "上次刷新时间"/"上次加载时间"前缀的资源
 */
@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
internal fun obtainLastActiveTime(
    isActive: Boolean,
    prefixRes: StringResource,
): String {
    var lastActiveTime by remember {
        mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
    }
    LaunchedEffect(isActive) {
        if (isActive) {
            lastActiveTime = Clock.System.now().toEpochMilliseconds()
        }
    }
    val pattern = stringResource(Res.string.usr_time_format_pattern)
    val prefix = stringResource(prefixRes)
    return remember(lastActiveTime, pattern) {
        val dateTime = Instant.fromEpochMilliseconds(lastActiveTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        prefix + LocalDateTime.Format { byUnicodePattern(pattern) }.format(dateTime)
    }
}
