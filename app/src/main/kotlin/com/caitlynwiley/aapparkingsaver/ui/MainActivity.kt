package com.caitlynwiley.aapparkingsaver.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caitlynwiley.aapparkingsaver.Prefs
import com.caitlynwiley.aapparkingsaver.theme.*
import com.caitlynwiley.aapparkingsaver.viewmodel.ParkingViewModel
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Prefs(this)

        setContent {
            AAPParkingSaverTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm by viewModels<ParkingViewModel>(
                        factoryProducer = { ParkingViewModel.Factory(prefs) }
                    )
                    val savedParkingLevel by vm.parkingLevel
                    val timeLevelSaved by vm.lastUpdatedTimestamp

                    if (isTimestampFromToday(timeLevelSaved) && savedParkingLevel in (2..9)) {
                        DisplayLevel(savedParkingLevel)
                    } else {
                        // nothing saved for today
                        var showNoLevelSavedMsg by remember { mutableStateOf(OffsetDateTime.now().hour >= 15) }

                        if (showNoLevelSavedMsg) { // if it's after 3pm
                            NoLevelSaved(dismiss = { showNoLevelSavedMsg = false })
                        } else {
                            // No level saved yet today, show level picker
                            DeckLevelOptions()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconTextButton(modifier: Modifier = Modifier, icon: ImageVector, iconDesc: String, text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button (
        modifier = modifier.padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White),
        border = BorderStroke(width = 2.dp, color = Color.White),
        contentPadding = PaddingValues(horizontal = 8.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(modifier = Modifier.padding(end = 4.dp).requiredSize(24.dp), imageVector = icon, contentDescription = iconDesc)
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