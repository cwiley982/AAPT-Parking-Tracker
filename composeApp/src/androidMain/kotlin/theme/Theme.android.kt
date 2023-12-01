package theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val useDynamicColor = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = if (darkTheme) {
        if (useDynamicColor) {
            dynamicDarkColorScheme(LocalContext.current)
        } else {
            darkColorScheme().copy(
                primary = DarkColors.primary,
                secondary = DarkColors.secondary,
                background = DarkColors.background,
                onBackground = DarkColors.onBackground,
            )
        }
    } else {
        if (useDynamicColor) {
            dynamicLightColorScheme(LocalContext.current)
        } else {
            lightColorScheme().copy(
                primary = LightColors.primary,
                secondary = LightColors.secondary,
                background = LightColors.background,
                onBackground = LightColors.onBackground,
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
