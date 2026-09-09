package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged

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
 * 记录 Header/Footer 指示器实际放置后的像素高度，供阈值计算与动画定位使用。
 *
 * 通过 [Modifier.onSizeChanged] 在真实布局后回写高度（首帧为 0，布局完成后即正确），
 * 相比 SubcomposeLayout 预测量方案，指示器只组合一次、
 * 且指示器高度变化不会触发内容重新测量。
 */
@Composable
internal fun rememberIndicatorHeights(): IndicatorHeights = remember { IndicatorHeights() }

/**
 * 指示器高度持有者
 */
internal class IndicatorHeights {
    var headerHeight by mutableIntStateOf(0)
    var footerHeight by mutableIntStateOf(0)
}

/**
 * 测量并上报 Header 指示器高度的 Modifier
 */
internal fun Modifier.onHeaderHeightChanged(heights: IndicatorHeights): Modifier =
    onSizeChanged { heights.headerHeight = it.height }

/**
 * 测量并上报 Footer 指示器高度的 Modifier
 */
internal fun Modifier.onFooterHeightChanged(heights: IndicatorHeights): Modifier =
    onSizeChanged { heights.footerHeight = it.height }

