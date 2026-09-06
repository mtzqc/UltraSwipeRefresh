package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints

/**
 * 获取Header的偏移量
 */
internal fun obtainHeaderOffset(
    state: UltraSwipeRefreshState,
    headerScrollMode: NestedScrollMode,
    headerHeight: Int
): Float {
    return when (headerScrollMode) {
        NestedScrollMode.FixedContent, NestedScrollMode.Translate -> state.indicatorOffset - headerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取内容的偏移量
 */
internal fun obtainContentOffset(
    state: UltraSwipeRefreshState,
    headerScrollMode: NestedScrollMode,
    footerScrollMode: NestedScrollMode
): Float {
    val indicatorOffset = state.indicatorOffset
    return if (indicatorOffset > 0f) {
        when (headerScrollMode) {
            NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> indicatorOffset
            NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 0f
        }
    } else {
        when (footerScrollMode) {
            NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> indicatorOffset
            NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 0f
        }
    }
}

/**
 * 获取Footer的偏移量
 */
internal fun obtainFooterOffset(
    state: UltraSwipeRefreshState,
    footerScrollMode: NestedScrollMode,
    footerHeight: Int
): Float {
    return when (footerScrollMode) {
        NestedScrollMode.Translate, NestedScrollMode.FixedContent -> state.indicatorOffset + footerHeight
        NestedScrollMode.FixedBehind, NestedScrollMode.FixedFront -> 0f
    }
}

/**
 * 获取Header或Footer的层级
 */
internal fun obtainZIndex(nestedScrollMode: NestedScrollMode): Float {
    return when (nestedScrollMode) {
        NestedScrollMode.FixedContent, NestedScrollMode.FixedFront -> 1f
        NestedScrollMode.Translate, NestedScrollMode.FixedBehind -> 0f
    }
}

/**
 * 通过[SubcomposeLayout]测量子布局[headerIndicator]和[footerIndicator]的高度
 */
@Composable
internal fun RefreshSubComposeLayout(
    headerIndicator: @Composable () -> Unit,
    footerIndicator: @Composable () -> Unit,
    content: @Composable (headerHeight: Int, footerHeight: Int) -> Unit
) {
    SubcomposeLayout { constraints: Constraints ->

        val headerMeasurable = subcompose(
            slotId = "headerIndicator",
            content = headerIndicator
        ).firstOrNull()?.measure(constraints)

        val footerMeasurable = subcompose(
            slotId = "footerIndicator",
            content = footerIndicator
        ).firstOrNull()?.measure(constraints)

        val contentMeasurable = subcompose(
            slotId = "content",
            content = {
                content(
                    headerMeasurable?.height ?: 0,
                    footerMeasurable?.height ?: 0
                )
            }).map { it.measure(constraints) }.first()

        layout(width = contentMeasurable.width, height = contentMeasurable.height) {
            contentMeasurable.placeRelative(0, 0)
        }
    }
}
