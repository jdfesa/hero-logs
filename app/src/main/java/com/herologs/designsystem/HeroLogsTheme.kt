package com.herologs.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Ink = Color(0xFF302A24)
val Paper = Color(0xFFF7EFD9)
val Surface = Color(0xFFFFF9EC)
val Sand = Color(0xFFE4D7B7)
val MutedInk = Color(0xFF756B5C)
val Vitality = Color(0xFFE86852)
val Joy = Color(0xFFF2AB39)
val Drive = Color(0xFF32998E)
val Calm = Color(0xFF6E96C0)
val Connection = Color(0xFFC4779B)
val Meaning = Color(0xFF8571BB)

private val HeroLogsColors = lightColorScheme(
    primary = Ink,
    onPrimary = Surface,
    secondary = Drive,
    background = Paper,
    onBackground = Ink,
    surface = Surface,
    onSurface = Ink,
    outline = Ink,
)

@Composable
fun HeroLogsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HeroLogsColors,
        typography = HeroLogsTypography,
        content = content,
    )
}
