package com.caitlynwiley.aapparkingsaver.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.Constants
import com.caitlynwiley.aapparkingsaver.geo.GeofenceReceiver
import com.caitlynwiley.aapparkingsaver.theme.chooseLevelPrompt
import com.caitlynwiley.aapparkingsaver.theme.levelOptionsStyle
import com.caitlynwiley.aapparkingsaver.ui.getBackgroundColor
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date

@Preview(showBackground = true)
@Composable
fun DeckLevelOptions() {
    Column {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp).weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                BasicText(
                    text = "Select which level you parked on today:",
                    style = chooseLevelPrompt.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            (9 downTo 2).map { level: Int ->
                Row(modifier = Modifier.weight(1f)) {
                    DeckLevelButton(level)
                }
            }
        }
    }
}

@Composable
fun DeckLevelButton(level: Int) {
    val levelColor = remember(level) { getBackgroundColor(level) }
    val vm = viewModel<ParkingViewModel>()

    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        colors = ButtonDefaults.buttonColors(containerColor = levelColor),
        onClick = {
            vm.updateParkingLevel(level)
            trackVehicleInDeck()
        }
    ) {
        BasicText(modifier = Modifier.wrapContentHeight(), text = "$level", style = levelOptionsStyle)
    }
}

@SuppressLint("SimpleDateFormat")
private fun trackVehicleInDeck() {
    val db = Firebase.database
    val reference = db.getReference("aapt")
    val p = reference.push()
    println("key from pushed ref: ${p.key}")
    val date = Date()
    val dateString = SimpleDateFormat(Constants.DATE_FORMAT).format(date)
    val timeString = SimpleDateFormat(Constants.TIME_FORMAT).format(date)
    p.setValue(GeofenceReceiver.ParkingCounter(1, dateString, timeString))
}