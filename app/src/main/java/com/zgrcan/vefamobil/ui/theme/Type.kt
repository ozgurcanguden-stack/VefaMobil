package com.zgrcan.vefamobil.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.zgrcan.vefamobil.R

val RedHatDisplayFontFamily = FontFamily(
    Font(R.font.red_hat_display_regular, FontWeight.Normal),
    Font(R.font.red_hat_display_medium, FontWeight.Medium),
    Font(R.font.red_hat_display_semibold, FontWeight.SemiBold),
    Font(R.font.red_hat_display_bold, FontWeight.Bold),
    Font(R.font.red_hat_display_black, FontWeight.Black),
)

private val BaseTypography = Typography()

val VefaTypography = Typography(
    displayLarge = BaseTypography.displayLarge.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Black,
    ),
    displayMedium = BaseTypography.displayMedium.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Black,
    ),
    displaySmall = BaseTypography.displaySmall.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineLarge = BaseTypography.headlineLarge.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineMedium = BaseTypography.headlineMedium.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineSmall = BaseTypography.headlineSmall.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    titleLarge = BaseTypography.titleLarge.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = BaseTypography.titleMedium.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    titleSmall = BaseTypography.titleSmall.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = BaseTypography.bodyLarge.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    bodyMedium = BaseTypography.bodyMedium.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    bodySmall = BaseTypography.bodySmall.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.Medium,
    ),
    labelLarge = BaseTypography.labelLarge.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    labelMedium = BaseTypography.labelMedium.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
    labelSmall = BaseTypography.labelSmall.copy(
        fontFamily = RedHatDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
    ),
)
