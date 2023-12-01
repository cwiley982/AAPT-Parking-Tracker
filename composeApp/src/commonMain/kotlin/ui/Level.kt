package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

typealias Callback = () -> Unit

@Composable
fun Level(
    level: Int,
    showMap: Boolean,
    onClear: Callback,
    x: Float,
    y: Float,
    editing: Boolean,
    hasPosition: Boolean,
    hasSelection: Boolean,
    onPosition: (x: Float, y: Float) -> Unit,
    onEdit: Callback,
    onSave: Callback,
    onCancel: Callback,
    onDelete: Callback
) {
    val backgroundColor = remember(level) { getBackgroundColor(level) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.Start
        ) {
            IconTextButton(
                icon = Icons.Outlined.Refresh,
                iconDesc = "refresh icon",
                text = "Reset Level",
                onClick = onClear
            )
        }

        Row(modifier = Modifier.fillMaxSize().weight(0.5f, true)) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                val fontSize =(if (maxHeight.value > 400) 400.sp else 300.sp)
                Text(
                    text = "$level",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(
                        textAlign = TextAlign.Center
                    ),
                    fontSize = fontSize
                )
            }
        }
        if (showMap) {
            Row(modifier = Modifier.fillMaxSize().weight(0.5f, true)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FloorPlan(x, y, onPosition)
                }
            }
        }

        BottomButtons(
            editing = editing,
            showMap = showMap,
            hasPosition = hasPosition,
            hasSelection = hasSelection,
            onEdit = onEdit,
            onSave = onSave,
            onCancel = onCancel,
            onDelete = onDelete
        )
    }
}