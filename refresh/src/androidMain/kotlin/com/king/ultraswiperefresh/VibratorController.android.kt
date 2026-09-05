package com.king.ultraswiperefresh

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android 平台振动控制器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Suppress("DEPRECATION")
@Composable
internal actual fun rememberVibratorController(): VibratorController? {
    val context = LocalContext.current
    return remember {
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        AndroidVibratorController(vibrator)
    }
}

@Suppress("DEPRECATION")
private class AndroidVibratorController(private val vibrator: Vibrator) : VibratorController {

    override fun hasVibrator(): Boolean = vibrator.hasVibrator()

    override fun vibrate(millis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    millis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(millis)
        }
    }
}
