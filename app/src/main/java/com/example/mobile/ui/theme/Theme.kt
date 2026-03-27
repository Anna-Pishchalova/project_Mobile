package com.example.mobile.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

private val PrimaryLight = Color(0xFF6750A4)
private val PrimaryDark = Color(0xFFD0BCFF)
private val SecondaryLight = Color(0xFF625B71)
private val SecondaryDark = Color(0xFFCCC2DC)
private val TertiaryLight = Color(0xFF7D5260)
private val TertiaryDark = Color(0xFFEFB8C8)
private val BackgroundLight = Color(0xFFFEF7FF)
private val BackgroundDark = Color(0xFF1C1B1F)

private val SurfaceLight = Color(0xFFFEF7FF)
private val SurfaceDark = Color(0xFF1C1B1F)

val BuildingMain = Color(0xFF4CAF50)
val BuildingA = Color(0xFF2196F3)
val BuildingB = Color(0xFFFF9800)
val BuildingLab = Color(0xFF9C27B0)
val BuildingDefault = Color(0xFF607D8B)
val LessonTypeLecture = Color(0xFF4CAF50)
val LessonTypePractice = Color(0xFF2196F3)
val LessonTypeLab = Color(0xFFFF9800)

@Composable
fun CollegeScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= 31 -> {
            dynamicLightColorScheme(LocalView.current.context)
        }

        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }
    val customColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = PrimaryDark,
            secondary = SecondaryDark,
            tertiary = TertiaryDark,
            background = BackgroundDark,
            surface = SurfaceDark,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = PrimaryLight,
            secondary = SecondaryLight,
            tertiary = TertiaryLight,
            background = BackgroundLight,
            surface = SurfaceLight,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF1C1B1F),
            onSurface = Color(0xFF1C1B1F)
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = customColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = customColorScheme,
        shapes = AppShapes,
        content = content
    )
}
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)