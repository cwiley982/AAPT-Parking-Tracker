package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoLevelSaved(dismiss: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "Hmm... It doesn't look like there's a parking level saved for today.",
            color = MaterialTheme.colorScheme.onBackground,
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
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            contentPadding = PaddingValues(horizontal = 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            onClick = dismiss
        ) {
            Text(
                text = "Back",
                fontSize = 18.sp
            )
        }
    }
}
