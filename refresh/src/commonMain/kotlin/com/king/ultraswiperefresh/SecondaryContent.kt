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
    SecondaryContent(
        state = state,
        boxSize = boxSize,
        isFooter = false,
        secondaryEnabled = headerSecondaryEnabled,
        secondaryBehavior = headerSecondaryBehavior,
        secondaryPreview = headerSecondaryPreview,
        secondaryContent = headerSecondaryContent,
    )
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
    SecondaryContent(
        state = state,
        boxSize = boxSize,
        isFooter = true,
        secondaryEnabled = footerSecondaryEnabled,
        secondaryBehavior = footerSecondaryBehavior,
        secondaryPreview = footerSecondaryPreview,
        secondaryContent = footerSecondaryContent,
    )
}

/**
 * Header/Footer 二级内容的共享实现；二者交互互为镜像，仅状态字段与偏移方向不同
 */
@Composable
private fun SecondaryContent(
    state: UltraSwipeRefreshState,
    boxSize: IntSize,
    isFooter: Boolean,
    secondaryEnabled: Boolean,
    secondaryBehavior: SecondaryBehavior,
    secondaryPreview: Boolean,
    secondaryContent: (@Composable (UltraSwipeRefreshState) -> Unit)?,
) {
    if (!secondaryEnabled || secondaryContent == null) return

    val isSecondary = if (isFooter) {
        state.footerState == UltraSwipeFooterState.Secondary
    } else {
        state.headerState == UltraSwipeHeaderState.Secondary
    }
    val isReleaseToSecondary = if (isFooter) {
        state.footerState == UltraSwipeFooterState.ReleaseToSecondary
    } else {
        state.headerState == UltraSwipeHeaderState.ReleaseToSecondary
    }

    val showSecondary by remember(isReleaseToSecondary, secondaryPreview) {
        derivedStateOf {
            isSecondary || (secondaryPreview && isReleaseToSecondary)
        }
    }

    val transition = updateTransition(isSecondary)
    val hiddenOffset by transition.animateFloat {
        if (it) 0f else if (isFooter) boxSize.height.toFloat() else -boxSize.height.toFloat()
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = if (secondaryBehavior == SecondaryBehavior.Slide) {
                    if (isReleaseToSecondary) {
                        if (isFooter) boxSize.height + state.indicatorOffset
                        else -boxSize.height + state.indicatorOffset
                    } else {
                        hiddenOffset
                    }
                } else {
                    0f
                }
            }
            .zIndex(if (isSecondary) 1f else 0f)
    ) {
        AnimatedVisibility(
            visible = showSecondary,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            secondaryContent(state)
        }
    }
}
