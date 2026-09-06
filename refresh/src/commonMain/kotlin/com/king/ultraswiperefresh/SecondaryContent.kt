package com.king.ultraswiperefresh

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex

/**
 * Header 二级内容
 */
@Composable
internal fun HeaderSecondaryContent(
    state: UltraSwipeRefreshState,
    boxSize: IntSize,
    headerSecondaryEnabled: Boolean,
    headerSecondaryBehavior: SecondaryBehavior,
    headerSecondaryPreview: Boolean,
    headerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)?,
) {
    if (!headerSecondaryEnabled || headerSecondaryContent == null) return

    val showHeaderSecondary by remember(state.headerState, headerSecondaryPreview) {
        derivedStateOf {
            when {
                headerSecondaryEnabled -> {
                    when {
                        state.headerState == UltraSwipeHeaderState.Secondary -> true
                        headerSecondaryPreview && state.headerState == UltraSwipeHeaderState.ReleaseToSecondary -> true
                        else -> false
                    }
                }

                else -> false
            }
        }
    }

    val headerTransaction = updateTransition(state.headerState == UltraSwipeHeaderState.Secondary)
    val headerOffset by headerTransaction.animateFloat { if (it) 0f else -boxSize.height.toFloat() }

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = if (headerSecondaryBehavior == SecondaryBehavior.Slide) {
                    if (state.headerState == UltraSwipeHeaderState.ReleaseToSecondary) {
                        -boxSize.height + state.indicatorOffset
                    } else {
                        headerOffset
                    }
                } else {
                    0f
                }
            }
            .zIndex(if (state.headerState == UltraSwipeHeaderState.Secondary) 1f else 0f)
    ) {
        AnimatedVisibility(
            visible = showHeaderSecondary,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            headerSecondaryContent(state)
        }
    }
}

/**
 * Footer 二级内容
 */
@Composable
internal fun FooterSecondaryContent(
    state: UltraSwipeRefreshState,
    boxSize: IntSize,
    footerSecondaryEnabled: Boolean,
    footerSecondaryBehavior: SecondaryBehavior,
    footerSecondaryPreview: Boolean,
    footerSecondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)?,
) {
    if (!footerSecondaryEnabled || footerSecondaryContent == null) return

    val showFooterSecondary by remember(state.footerState, footerSecondaryPreview) {
        derivedStateOf {
            when {
                footerSecondaryEnabled -> {
                    when {
                        state.footerState == UltraSwipeFooterState.Secondary -> true
                        footerSecondaryPreview && state.footerState == UltraSwipeFooterState.ReleaseToSecondary -> true
                        else -> false
                    }
                }

                else -> false
            }
        }
    }

    val footerTransaction = updateTransition(state.footerState == UltraSwipeFooterState.Secondary)
    val footerOffset by footerTransaction.animateFloat { if (it) 0f else boxSize.height.toFloat() }

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = if (footerSecondaryBehavior == SecondaryBehavior.Slide) {
                    if (state.footerState == UltraSwipeFooterState.ReleaseToSecondary) {
                        boxSize.height + state.indicatorOffset
                    } else {
                        footerOffset
                    }
                } else {
                    0f
                }
            }
            .zIndex(if (state.footerState == UltraSwipeFooterState.Secondary) 1f else 0f)
    ) {
        AnimatedVisibility(
            visible = showFooterSecondary,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            footerSecondaryContent(state)
        }
    }
}
