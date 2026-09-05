package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

/**
 * iOS 平台触觉反馈控制器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Composable
internal actual fun rememberVibratorController(): VibratorController? {
    return remember { IosVibratorController() }
}

private class IosVibratorController : VibratorController {

    private val generator = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)

    override fun hasVibrator(): Boolean = true

    override fun vibrate(millis: Long) {
        generator.impactOccurred()
    }
}
