package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.chooseLevelPrompt
import theme.levelOptionsStyle

@Composable
fun LevelPicker(onPick: (Int) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            BasicText(
                text = "Select which level you parked on today:",
                style = chooseLevelPrompt.copy(color = MaterialTheme.colors.onBackground)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        (9 downTo 2).map {level: Int ->
            Row(modifier = Modifier.weight(1f)) {
                LevelButton(level, onPick)
            }
        }
    }
}

@Composable
fun LevelButton(level: Int, onPick: (Int) -> Unit) {
    val levelColor = remember(level) { getBackgroundColor(level) }
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(50),
        colors = buttonColors(
            contentColor = levelColor,
            backgroundColor = levelColor
        ),
        onClick = {
            onPick(level)
        }
    ) {
        AutoResizeText(
            text = "$level",
            fontSizeRange = FontSizeRange(40.sp, 80.sp),
            style = levelOptionsStyle,
        )
    }
}

