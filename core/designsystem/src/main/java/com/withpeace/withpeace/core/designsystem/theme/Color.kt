package com.withpeace.withpeace.core.designsystem.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val mainPurple = Color(0xFF9A70E2)
val subPurple = Color(0xFFE8E8FC)
val subSkyBlue = Color(0xFF90EFEF)
val subBlueGreen = Color(0xFFBDE4DF)
val subBlue1 = Color(0xFFDFF2F9)
val subBlue2 = Color(0xFF0575E6)

val systemBlack = Color(0xFF212529)
val systemWhite = Color.White
val systemGray1 = Color(0xFF3D3D3D)
val systemGray2 = Color(0xFFA7A7A7)
val systemGray3 = Color(0xFFECECEF)
val systemGray4 = Color(0xFF696969)
val systemError = Color(0xFFF0474B)
val systemSuccess = Color(0xFF3BD569)
val systemHyperLink = Color(0xFF20BCBB)

data class WithPeaceColor(
    val MainPurple: Color = mainPurple,
    val SubPurple: Color = subPurple,
    val SubSkyBlue: Color = subSkyBlue,
    val SubBlueGreen: Color = subBlueGreen,
    val SubBlue1: Color = subBlue1,
    val SubBlue2: Color = subBlue2,
    val SystemBlack: Color = systemBlack,
    val SystemWhite: Color = systemWhite,
    val SystemGray1: Color = systemGray1,
    val SystemGray2: Color = systemGray2,
    val SystemGray3: Color = systemGray3,
    val SystemError: Color = systemError,
    val SystemGray4: Color = systemGray4,
    val SystemSuccess: Color = systemSuccess,
    val SystemHyperLink: Color = systemHyperLink,
)

val lightColor = WithPeaceColor()
val darkColor = WithPeaceColor()
