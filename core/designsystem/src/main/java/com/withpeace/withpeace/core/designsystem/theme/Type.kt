package com.withpeace.withpeace.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.withpeace.withpeace.core.designsystem.R

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
         */
    )

val NotoSansFont =
    FontFamily(
        Font(
            resId = R.font.notosans_kr_medium,
            weight = FontWeight.Bold,
        ),
    )

val PretendardFont =
    FontFamily(
        Font(
            resId = R.font.pretendard_bold,
            weight = FontWeight.Bold,
        ),
        Font(
            resId = R.font.pretendard_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.pretendard_extra_bold,
            weight = FontWeight.ExtraBold,
        ),
        Font(
            resId = R.font.pretendard_extra_light,
            weight = FontWeight.ExtraLight,
        ),
        Font(
            resId = R.font.pretendard_extra_light,
            weight = FontWeight.Light,
        ),
        Font(
            resId = R.font.pretendard_medium,
            weight = FontWeight.Medium,
        ),
        Font(
            resId = R.font.pretendard_semi_bold,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resId = R.font.pretendard_thin,
            weight = FontWeight.Thin,
        ),
    )

@Immutable
data class WithPeaceTypography(
    val notoSans: TextStyle =
        TextStyle(
            fontFamily = NotoSansFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 23.17.sp,
        ),
    val heading: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 33.6.sp,
            letterSpacing = (-0.096).sp,
        ),
    val title1: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.08).sp,
        ),
    val title2: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 25.2.sp,
            letterSpacing = (-0.4).sp,
        ),
    val body: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 22.4.sp,
            letterSpacing = (-0.4).sp,
        ),
    val caption: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 19.6.sp,
            letterSpacing = (-0.4).sp,
        ),

    val homePolicyTitle: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            letterSpacing = (0.16).sp,
        ),
    val homePolicyContent: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
    val homePolicyTag: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
        ),
    val policyDetailCaption: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
    val bold16Sp: TextStyle =
        TextStyle(
            fontFamily = PretendardFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp
        ),

)
