package theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}