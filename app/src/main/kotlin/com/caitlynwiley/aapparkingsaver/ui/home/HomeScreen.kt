package com.caitlynwiley.aapparkingsaver.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.Prefs
import com.caitlynwiley.aapparkingsaver.ui.isTimestampFromToday
import java.time.OffsetDateTime

@Composable
fun HomeScreen() {
    val vm = viewModel<ParkingViewModel>(
        factory = ParkingViewModel.Factory(Prefs)
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

