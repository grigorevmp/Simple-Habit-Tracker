package com.grigorevmp.habits.presentation.screen.common

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.random.Random

@Composable
fun EmojiView(
    emoji: String,
    fontSize: Float = 48.0F
) {
    AndroidView(
        factory = { context ->
            AppCompatTextView(context).apply {
                setTextColor(Color.Black.toArgb())
                text = emoji
                textSize = fontSize
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        },
        update = {
            it.apply {
                text = emoji
            }
        },
    )
}

fun getRandomLikeEmoji(seed: Long) = listOf("⚡", "🫰", "🩶", "🤍", "🤎", "💛", "🧡", "💖", "❤️", "🩵", "💜", "💙", "💚", "❤️‍🔥", "🔥", "🧨", "✨", "🎉", "🎊").random(Random(seed))
