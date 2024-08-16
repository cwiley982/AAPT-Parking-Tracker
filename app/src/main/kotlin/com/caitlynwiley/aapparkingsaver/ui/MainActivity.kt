package com.caitlynwiley.aapparkingsaver.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.PermissionsRepo
import com.caitlynwiley.aapparkingsaver.theme.AAPParkingSaverTheme
import com.caitlynwiley.aapparkingsaver.theme.Level2Orange
import com.caitlynwiley.aapparkingsaver.theme.Level3Blue
import com.caitlynwiley.aapparkingsaver.theme.Level4Yellow
import com.caitlynwiley.aapparkingsaver.theme.Level5Green
import com.caitlynwiley.aapparkingsaver.theme.Level6Purple
import com.caitlynwiley.aapparkingsaver.theme.Level7Red
import com.caitlynwiley.aapparkingsaver.theme.Level8Blue
import com.caitlynwiley.aapparkingsaver.theme.Level9Yellow
import com.caitlynwiley.aapparkingsaver.theme.ThemeRepository
import com.caitlynwiley.aapparkingsaver.ui.home.PermissionsViewModel
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val matchSystemTheme by ThemeRepository.useSystemTheme.collectAsState()
            val darkModeEnabled by ThemeRepository.useDarkMode.collectAsState()
            val systemInDarkMode = isSystemInDarkTheme()

            val inDarkMode by remember (matchSystemTheme, darkModeEnabled) {
                mutableStateOf(
                    if (matchSystemTheme) systemInDarkMode
                    else darkModeEnabled
                )
            }

            AAPParkingSaverTheme(useDarkMode = inDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScreen()

                    PermissionsUi()
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    @Composable
    fun PermissionsUi() {
        val vm = viewModel<PermissionsViewModel>()
        val permissionsRequired by vm.requireLocationPermissions.collectAsState()

        if (permissionsRequired) {
            val hasLocationPerms by vm.hasLocationPerms.collectAsState()
            val hasBackgroundPerm by vm.hasBackgroundPerm.collectAsState()

            var requestedPermissions by remember { mutableStateOf(false) }
            var requestedBackgroundPermission by remember { mutableStateOf(false) }

            if (!hasLocationPerms && !requestedPermissions) {
                println("requesting permissions via system launcher")
                requestedPermissions = true
                requestMultiPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }

            if (hasLocationPerms && !hasBackgroundPerm && !requestedBackgroundPermission) {
                requestedBackgroundPermission = true
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    BackgroundLocationRequestDialog {
                        println("requesting background permission")
                        requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
                } else {
                    println("requesting background permission")
                    requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }
    }

    @Composable
    fun BackgroundLocationRequestDialog(requestPermission: () -> Unit) {
        val context = LocalContext.current
        var dismissed by remember { mutableStateOf(false) }

        if (!dismissed) {
            AlertDialog(
                onDismissRequest = {
                    Toast.makeText(context, "but I neeeeed it... :(", Toast.LENGTH_LONG).show()
                    dismissed = true
                },
                confirmButton = {
                    Button(onClick = { requestPermission() }) {
                        Text("Allow")
                    }
                },
                title = { Text("me need your location >:D") },
                text = {
                    Text("We need access to your location when the app is in the background to send reminders to save your parking when you arrive. Please select 'Allow all the time' on the next screen to let this app access your location in the background.")
                }
            )
        }
    }

    @SuppressLint("MissingPermission")
    val requestMultiPermissionLauncher = registerForActivityResult(RequestMultiplePermissions()) {
        PermissionsRepo.recheckPermissions(this)
    }

    @SuppressLint("MissingPermission")
    val requestSinglePermissionLauncher = registerForActivityResult(RequestPermission()) {
        PermissionsRepo.recheckPermissions(this)
    }
}

@Composable
fun IconTextButton(modifier: Modifier = Modifier, icon: ImageVector, iconDesc: String, text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button (
        modifier = modifier.padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(width = 2.dp, color = Color.White),
        contentPadding = PaddingValues(horizontal = 8.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .requiredSize(24.dp),
            imageVector = icon,
            contentDescription = iconDesc
        )
        Text(modifier = modifier.padding(end = 4.dp), text = text, fontSize = 18.sp)
    }
}

fun getBackgroundColor(level: Int): Color {
    return when (level) {
        2 -> Level2Orange
        3 -> Level3Blue
        4 -> Level4Yellow
        5 -> Level5Green
        6 -> Level6Purple
        7 -> Level7Red
        8 -> Level8Blue
        9 -> Level9Yellow
        else -> Color.LightGray
    }
}

fun isTimestampFromToday(timestamp: Long): Boolean {
    val now = OffsetDateTime.now()
    val todayStartTime = now.withHour(0).withMinute(0)
        .withSecond(0).withNano(0).toEpochSecond() * 1000

    return timestamp > todayStartTime
}

/*
- When adding location, add scrim over rest of screen so the map is "highlighted" to indicate
user should interact.
   - Add 'Save' and 'Cancel' buttons to save location & remove scrim, or cancel with no
   location saved and map hidden, and also remove scrim.
*/