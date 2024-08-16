package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.caitlynwiley.aapparkingsaver.theme.AAPParkingSaverTheme
import com.caitlynwiley.aapparkingsaver.ui.home.DeckLevelOptions
import com.caitlynwiley.aapparkingsaver.ui.home.DisplayLevel
import com.caitlynwiley.aapparkingsaver.ui.home.NoLevelSaved
import com.caitlynwiley.aapparkingsaver.ui.settings.SettingsScreen

@Preview(showBackground = true)
@Composable
fun NothingSavedPreview() {
    AAPParkingSaverTheme {
        NoLevelSaved {}
    }
}

@Preview(showBackground = true)
@Composable
fun LevelChooserPreview() {
    AAPParkingSaverTheme {
        DeckLevelOptions()
    }
}

@Preview(showBackground = true)
@Composable
fun ChosenLevelPreview() {
    AAPParkingSaverTheme {
        DisplayLevel(3)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    AAPParkingSaverTheme {
        SettingsScreen()
    }
}