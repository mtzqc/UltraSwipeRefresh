package com.king.ultraswiperefresh

import androidx.compose.runtime.Composable

/**
 * 振动控制器抽象（跨平台）
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
internal interface VibratorController {

    /**
     * 设备是否支持振动
     */
    fun hasVibrator(): Boolean

    /**
     * 触发一次指定时长的振动
     */
    fun vibrate(millis: Long)
}

/**
 * 获取当前平台的振动控制器；不支持振动的平台返回 null
 */
@Composable
internal expect fun rememberVibratorController(): VibratorController?
