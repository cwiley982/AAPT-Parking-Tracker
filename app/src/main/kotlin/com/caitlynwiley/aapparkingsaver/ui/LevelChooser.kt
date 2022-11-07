package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.theme.chooseLevelPrompt
import com.caitlynwiley.aapparkingsaver.theme.levelOptionsStyle
import com.caitlynwiley.aapparkingsaver.viewmodel.ParkingViewModel

@Composable
fun DeckLevelOptions() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            BasicText(
                text = "Select which level you parked on today:",
                style = chooseLevelPrompt
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        (9 downTo 2).map { level: Int ->
            Row(modifier = Modifier.weight(1f)) {
                DeckLevelButton(level)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DeckLevelButton(level: Int) {
    val levelColor = remember(level) { getBackgroundColor(level) }
    val vm = viewModel<ParkingViewModel>()

    Button(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = levelColor),
        onClick = {
            vm.updateParkingLevel(level)
        }
    ) {
        BasicText(text = "$level", style = levelOptionsStyle)
    }
}