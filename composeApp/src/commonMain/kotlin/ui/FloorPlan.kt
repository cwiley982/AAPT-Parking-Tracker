package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FloorPlan(x: Float, y: Float, onPosition: (x: Float, y: Float) -> Unit) {
    val density = LocalDensity.current.density
    val carSize = 48.dp

    val inEditMode = false // by vm.inEditMode

    Box(modifier = Modifier
        .fillMaxWidth(0.8f)
        .wrapContentHeight()
    ) {
        // Map Image
        Image (
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(inEditMode) {
                    detectTapGestures {
                        onPosition(it.x / density, it.y /density)
                    }
                },
            painter = painterResource("deck.xml"),
            contentDescription = "Parking deck floor plan",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center
        )

        // Car Icon
        if (x > 0 && y > 0) {
            Icon(
                modifier = Modifier
                    .size(carSize)
                    .align(Alignment.TopStart)
                    .offset(
                        x = x.dp - (carSize / 2),
                        y = y.dp - (carSize / 2)
                    ),
                painter = painterResource("car.xml"),
                tint = Color.White, contentDescription = "car icon"
            )
        }
    }
}