package com.instagramclone.util.constants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object Utils {

    // Colors
    /**
     * Adaptive background color for Dark and Light mode.
     */
    val IgBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0XFF000000) else Color(0xFFFFFFFF)

    /**
     * Adaptive off background color for Dark and Light mode.
     */
    val IgOffBackground: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF272829) else Color(0xFFE7DDDD)

    /**
     * Adaptive button color for Dark and Light mode.
     */
    val IgButtonColor: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF2A2B2C) else Color(0xFFD8DEE4)

    /**
     * Adaptive off black and white color for Dark and Light mode. Note: This is opposite color same as onBackground.
     */
    val IgOffColor: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFF2A2B2C) else Color(0xFFEED4D4)

    val IgBlack = Color(0XFF000000)
    val IgBlue = Color(0xFF2196F3)
    val IgError = Color(0xFFE95044)
    val IgLikeRed = Color(0xFFF55043)

    /**
     * Adaptive hyperlink color for Dark and Light mode.
     */
    val IgLinkBlue: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFA1DCF7) else Color(0xFF5E39A0)

    /**
     * Adaptive list of color for Dark and Light mode.
     */
    val IgAccountsCenterColors: List<Color>
        @Composable
        get() = if (isSystemInDarkTheme()) {
            listOf(
                Color(0xFF383131),
                Color(0xFF261B38),
                Color(0xFF261B38),
                Color(0xFF261B38),
                Color(0xFF383131)
            )
        } else {
            listOf(
                Color(0xFFF0E0E0),
                Color(0xFFD3C3EC),
                Color(0xFFE2D9F1),
                Color(0xFFD7CAEC),
                Color(0xFFF1E9E9)
            )
        }

    /**
     * Adaptive card color for Dark and Light mode.
     */
    val IgAccountsCenterCardColor: Color
        @Composable
        get() = if (isSystemInDarkTheme()) Color(0x92383131) else Color(0xFFFFFFFF)

    /**
     * Adaptive font color for Dark and Light mode.
     */
    val IgFontColor: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) Color(0xFFE7DDDD) else Color(0xFF272829)
}