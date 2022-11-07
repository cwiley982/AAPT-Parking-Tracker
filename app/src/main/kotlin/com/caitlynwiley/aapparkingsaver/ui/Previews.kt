package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.caitlynwiley.aapparkingsaver.theme.AAPParkingSaverTheme

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