package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoLevelSaved(onDismiss: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "Hmm... It doesn't look like there's a parking level saved for today.",
            color = MaterialTheme.colors.onBackground,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Button (
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.onBackground
            ),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.onBackground),
            contentPadding = PaddingValues(horizontal = 8.dp),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            onClick = onDismiss
        ) {
            Text(
                text = "Back",
                fontSize = 18.sp
            )
        }
    }
}