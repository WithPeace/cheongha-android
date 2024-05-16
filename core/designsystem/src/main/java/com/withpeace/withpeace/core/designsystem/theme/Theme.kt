package com.withpeace.withpeace.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCustomColors =
    staticCompositionLocalOf {
        WithPeaceColor()
    }

val LocalCustomTypography =
    staticCompositionLocalOf {
        WithPeaceTypography()
    }
val LocalCustomPadding =
    staticCompositionLocalOf {
        WithPeacePadding()
    }

@Composable
fun WithpeaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            darkTheme -> darkColor
            else -> lightColor
        }
    CompositionLocalProvider(
        LocalCustomColors provides colorScheme,
        LocalCustomTypography provides WithPeaceTypography(),
        LocalCustomPadding provides WithPeacePadding(),
        content = content,
    )
}

object WithpeaceTheme {
    val colors: WithPeaceColor
        @Composable
        get() = LocalCustomColors.current
    val typography: WithPeaceTypography
        @Composable
        get() = LocalCustomTypography.current
    val padding: WithPeacePadding
        @Composable
        get() = LocalCustomPadding.current
}
