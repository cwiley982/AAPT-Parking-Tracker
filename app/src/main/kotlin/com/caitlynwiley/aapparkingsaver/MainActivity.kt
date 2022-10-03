package com.caitlynwiley.aapparkingsaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caitlynwiley.aapparkingsaver.ui.theme.*
import java.time.OffsetDateTime

val LocalSharedPrefs = compositionLocalOf { Prefs.DEFAULT_PREFS_IMPL }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Prefs(this)

        setContent {
            CompositionLocalProvider(
                LocalSharedPrefs provides prefs
            ) {
                AAPTowerParkingSaverTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val savedParkingLevel = prefs.parkingLevel
                        val lastUpdatedTime = prefs.lastUpdatedTimestamp
                        val now = OffsetDateTime.now()
                        val todayStartTime = now.withHour(0).withMinute(0)
                            .withSecond(0).withNano(0).toEpochSecond() * 1000

                        if (lastUpdatedTime > todayStartTime && savedParkingLevel in (2..9)) {
                            DisplayLevel(savedParkingLevel)
                        } else {
                            // nothing saved for today
                            if (now.hour >= 15) { // if it's after 3pm
                                NoLocationSaved()
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
}

@Composable
fun DeckLevelOptions() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
        (9 downTo 2).map { level: Int ->
            Row(modifier = Modifier.weight(1f)) {
                DeckLevelButton(level)
            }
        }
    }
}

@Composable
fun DeckLevelButton(level: Int) {
    val levelColor = remember(level) { getBackgroundColor(level) }
    val textColor = remember(level) { getTextColor(level) }
    val prefs = LocalSharedPrefs.current

    Button(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = levelColor),
        onClick = {
            prefs.parkingLevel = level
        }
    ) {
        Text(text = "$level", color = textColor, fontSize = 48.sp)
    }
}

@Composable
fun NoLocationSaved() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "Hmm... It doesn't look like there's a parking level saved for today.",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun DisplayLevel(level: Int = 0) {
    val backgroundColor = getBackgroundColor(level)
    val textColor = getTextColor(level)

    Box (modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
        Text (
            modifier = Modifier.align(Alignment.Center),
            text = "$level",
            fontSize = 360.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        val prefs = LocalSharedPrefs.current
        Button (
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = textColor),
            border = BorderStroke(width = 2.dp, color = textColor),
            contentPadding = PaddingValues(horizontal = 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            onClick = { prefs.clearSavedLevel() }
        ) {
            Text(
                text = "RESET",
                fontSize = 18.sp
            )
        }
    }
}

private fun getBackgroundColor(level: Int): Color {
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

private fun getTextColor(level: Int): Color {
    return when (level) {
        4, 9 -> Color.Black
        else -> Color.White
    }
}

@Preview(showBackground = true)
@Composable
fun NothingSavedPreview() {
    AAPTowerParkingSaverTheme {
        NoLocationSaved()
    }
}

@Preview(showBackground = true)
@Composable
fun LevelChooserPreview() {
    AAPTowerParkingSaverTheme {
        DeckLevelOptions()
    }
}

@Preview(showBackground = true)
@Composable
fun ChosenLevelPreview() {
    AAPTowerParkingSaverTheme {
        DisplayLevel(3)
    }
}