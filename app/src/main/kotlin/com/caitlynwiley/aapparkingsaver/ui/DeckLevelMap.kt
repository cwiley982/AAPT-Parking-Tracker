package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.R
import com.caitlynwiley.aapparkingsaver.viewmodel.ParkingViewModel

@Composable
fun DeckFloorPlanView() {
    val vm = viewModel<ParkingViewModel>()
    val density = LocalDensity.current
    val carSize = 48.dp

    val inEditMode by vm.inEditMode
    val carX by vm.carX
    val carY by vm.carY

    Box(modifier = Modifier
        .fillMaxWidth(0.8f)
        .wrapContentHeight()) {
        // Map Image
        Image (
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(inEditMode) {
                    detectTapGestures {
                        vm.updateTempCarPosition(x = it.x, y = it.y, density = density.density)
                    }
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_deck_layout_24),
            contentDescription = "Parking deck floor plan",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center
        )

        // Car Icon
        if (carX > 0 && carY > 0) {
            Icon(
                modifier = Modifier
                    .size(carSize)
                    .align(Alignment.TopStart)
                    .offset(
                        x = carX.dp - (carSize / 2),
                        y = carY.dp - (carSize / 2)
                    ),
                painter = painterResource(id = R.drawable.ic_outline_car_24),
                tint = Color.White, contentDescription = "car icon"
            )
        }
    }
}