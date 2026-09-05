package com.king.ultraswiperefresh.app.ext

import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import com.king.ultraswiperefresh.app.generated.resources.Res

/**
 * 从 composeResources 读取 Lottie 动画 JSON 的 Spec
 */
internal class ResourceLottieSpec(path: String) : LottieCompositionSpec {

    private val resourcePath = "files/$path"

    override val key: String? = resourcePath

    override suspend fun load(): LottieComposition {
        val json = Res.readBytes(resourcePath).decodeToString()
        return LottieCompositionSpec.JsonString(json).load()
    }
}
