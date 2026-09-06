package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * 振动效果反馈
 */
@Composable
internal fun VibrationLaunchedEffect(
    vibrationEnabled: Boolean,
    vibrationMillis: Long,
    state: UltraSwipeRefreshState
) {
    val vibrator = rememberVibratorController()

    if (!vibrationEnabled || vibrator == null || !vibrator.hasVibrator()) return

    LaunchedEffect(state.headerState, state.footerState) {
        if (state.headerState == UltraSwipeHeaderState.ReleaseToRefresh ||
            state.footerState == UltraSwipeFooterState.ReleaseToLoad ||
            state.headerState == UltraSwipeHeaderState.ReleaseToSecondary ||
            state.footerState == UltraSwipeFooterState.ReleaseToSecondary
        ) {
            vibrator.vibrate(vibrationMillis)
        }
    }
}
