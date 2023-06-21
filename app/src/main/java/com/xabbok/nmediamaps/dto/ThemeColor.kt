package com.xabbok.nmediamaps.dto

import android.graphics.Color
import androidx.core.graphics.ColorUtils

data class ThemeColors(
    val colorPrimary: Int,
    val colorPrimaryVariant: Int,
    val colorSecondary: Int,
    val colorSecondaryVariant: Int,
    val colorBackground: Int,
    val colorSurface: Int,
    val colorError: Int,
    val colorOnPrimary: Int,
    val colorOnSecondary: Int,
    val colorOnBackground: Int,
    val colorOnSurface: Int,
    val colorOnError: Int,
    val colorBrandPrimary: Int,
    val colorBrandSecondary: Int,
    val colorBrandBackgroundPrimary: Int,
    val colorBrandBackgroundSecondary: Int,
    val colorBrandSurfacePrimary: Int,
    val colorBrandSurfaceSecondary: Int,
    val colorBrandAccent: Int,
    val colorControlNormal: Int,
    val colorControlActivated: Int,
    val colorControlHighlight: Int,
    val colorButtonNormal: Int,
    val colorButtonDisabled: Int,
    val colorButtonFocused: Int,
    val colorButtonPressed: Int
)

fun getContrastColor(color: Int): Int {
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    val yiq = ((red * 299) + (green * 587) + (blue * 114)) / 1000
    return if (yiq >= 128) Color.BLACK else Color.WHITE
}


fun darken(color: Int, factor: Float = 0.7f): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[2] *= factor
    return Color.HSVToColor(hsv)
}

fun generateThemeColors(colorPrimary: Int): ThemeColors {
    val colorPrimaryDark = darken(colorPrimary) // темный оттенок цвета Primary
    val colorSecondary = ColorUtils.blendARGB(colorPrimary, Color.WHITE, 0.1f) // Secondary цвет, сочетается с Primary
    val colorSecondaryVariant = darken(colorSecondary) // темный оттенок Secondary
    val colorBackground = ColorUtils.blendARGB(colorPrimary, Color.BLACK, 0.05f) // Background цвет, сочетается с Primary
    val colorSurface = ColorUtils.blendARGB(colorPrimary, Color.WHITE, 0.9f) // Surface цвет, сочетается с Primary
    val colorError = Color.parseColor("#b00020") // заранее определенный Error цвет
    val colorOnPrimary = getContrastColor(colorPrimary) // Цвет текста на Primary фоне
    val colorOnSecondary = getContrastColor(colorSecondary) // Цвет текста на Secondary фоне
    val colorOnBackground = getContrastColor(colorBackground) // Цвет текста на Background фоне
    val colorOnSurface = getContrastColor(colorSurface) // Цвет текста на Surface фоне
    val colorOnError = getContrastColor(colorError) // Цвет текста на Error фоне
    val colorBrandPrimary = ColorUtils.blendARGB(colorPrimary, Color.WHITE, 0.5f) // Brand Primary цвет
    val colorBrandSecondary = darken(colorBrandPrimary) // Brand Secondary цвет
    val colorBrandBackgroundPrimary = ColorUtils.blendARGB(colorBrandPrimary, Color.BLACK, 0.05f) // Background цвет для Brand Primary
    val colorBrandBackgroundSecondary = ColorUtils.blendARGB(colorBrandSecondary, Color.BLACK, 0.05f) // Background цвет для Brand Secondary
    val colorBrandSurfacePrimary = ColorUtils.blendARGB(colorBrandPrimary, Color.WHITE, 0.9f) // Surface цвет для Brand Primary
    val colorBrandSurfaceSecondary = ColorUtils.blendARGB(colorBrandSecondary, Color.WHITE, 0.9f) // Surface цвет для Brand Secondary
    val colorBrandAccent = ColorUtils.blendARGB(colorPrimary, Color.WHITE, 0.2f) // Accent цвет, сочетается с Primary
    val colorControlNormal = colorSurface // Нормальный Control цвет, соответствует Surface
    val colorControlActivated = colorPrimary // Активный Control цвет, соответствует Primary
    val colorControlHighlight = darken(colorControlActivated) // Highlight цвет, темный оттенок активного Control цвета
    val colorButtonNormal = colorPrimary // Нормальный Button цвет, соответствует Primary
    val colorButtonDisabled = ColorUtils.setAlphaComponent(getContrastColor(colorSurface), 127) // Disabled Button цвет, прозрачный оттенок текста на Surface фоне
    val colorButtonFocused = darken(colorButtonNormal) // Focused Button цвет, темный оттенок нормального Button цвета
    val colorButtonPressed = darken(colorButtonFocused) // Pressed Button цвет, темный оттенок focused Button цвета

    return ThemeColors(
        colorPrimary,
        colorPrimaryDark,
        colorSecondary,
        colorSecondaryVariant,
        colorBackground,
        colorSurface,
        colorError,
        colorOnPrimary,
        colorOnSecondary,
        colorOnBackground,
        colorOnSurface,
        colorOnError,
        colorBrandPrimary,
        colorBrandSecondary,
        colorBrandBackgroundPrimary,
        colorBrandBackgroundSecondary,
        colorBrandSurfacePrimary,
        colorBrandSurfaceSecondary,
        colorBrandAccent,
        colorControlNormal,
        colorControlActivated,
        colorControlHighlight,
        colorButtonNormal,
        colorButtonDisabled,
        colorButtonFocused,
        colorButtonPressed
    )
}