package com.king.ultraswiperefresh.indicator.lottie

import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import com.king.ultraswiperefresh.indicator.lottie.generated.resources.Res

/**
 * 从 composeResources 读取默认 Lottie 动画的 Spec
 * （Compottie 无 RawRes/Asset 概念，动画 JSON 位于 commonMain/composeResources/files）
 */
internal object DefaultLottieSpec : LottieCompositionSpec {

    private const val RESOURCE_PATH = "files/usr_default_lottie_animation.json"

    override val key: String? = RESOURCE_PATH

    override suspend fun load(): LottieComposition {
        val json = Res.readBytes(RESOURCE_PATH).decodeToString()
        return LottieCompositionSpec.JsonString(json).load()
    }
}
