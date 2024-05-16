package com.withpeace.withpeace.core.designsystem.util

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dropShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    spreadRadius: Dp = 0.dp,
    modifier: Modifier = Modifier
) = then(
    modifier.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spreadRadius.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = size.width + spreadPixel
            val bottomPixel = size.height + spreadPixel

            frameworkPaint.color = color.toArgb()

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = BlurMaskFilter(
                    blurRadius.toPx(),
                    BlurMaskFilter.Blur.NORMAL
                )
            }

            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint = paint
            )
        }
    }
)
// Figma dropShadow 스펙을 구현하는 확장함수입니다.
// ref: https://spoqa.github.io/2023/10/30/android-jetpack-compose.html
