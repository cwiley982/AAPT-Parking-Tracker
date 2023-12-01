import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.platformLogWriter
import org.jetbrains.compose.resources.ExperimentalResourceApi

import screens.HomeScreen


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Logger.setLogWriters(platformLogWriter(NoTagFormatter))
    Navigator(HomeScreen)
}