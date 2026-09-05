package com.king.ultraswiperefresh.app.ext

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */

var toast: Toast? = null

fun Context.showToast(text: String) {
    toast?.cancel()
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast?.show()
}
@Composable
internal actual fun rememberToast(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { text: String -> context.showToast(text) }
    }
}
