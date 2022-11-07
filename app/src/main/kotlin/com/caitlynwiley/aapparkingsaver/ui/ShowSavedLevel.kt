package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.viewmodel.ParkingViewModel

@Composable
fun DisplayLevel(level: Int = 0) {
    val backgroundColor = getBackgroundColor(level)
    val vm = viewModel<ParkingViewModel>()
    val showMap by vm.showMap

    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconTextButton(icon = Icons.Outlined.Refresh, iconDesc = "refresh icon",
                text = "Reset Level", onClick = vm::clearSavedLevel)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                val levelTextSize = remember(showMap) { if (showMap) 300.sp else 360.sp }

                @Suppress("DEPRECATION")
                Text(
                    text = "$level",
                    fontSize = levelTextSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )

                if (showMap) {
                    DeckFloorPlanView()
                }
            }
        }

        BottomButtonRow()
    }
}