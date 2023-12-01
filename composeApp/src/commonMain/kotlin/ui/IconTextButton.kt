package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconDesc: String,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(width = 2.dp, color = Color.White),
        contentPadding = PaddingValues(horizontal = 8.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(end = 4.dp).requiredSize(24.dp),
            imageVector = icon,
            contentDescription = iconDesc
        )
        Text(
            modifier = modifier.padding( end = 4.dp),
            text = text,
            fontSize = 18.sp
        )
    }
}