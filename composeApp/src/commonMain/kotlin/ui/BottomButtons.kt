package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomButtons(
    editing: Boolean,
    showMap: Boolean,
    hasPosition: Boolean,
    hasSelection: Boolean,
    onEdit: Callback,
    onSave: Callback,
    onCancel: Callback,
    onDelete: Callback
) {
    val arrangement = if (editing) Arrangement.SpaceAround else Arrangement.End

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = arrangement
    ) {
        if (showMap) {
            if (editing) {
                EditingLocationButtons(
                    hasPosition = hasPosition,
                    hasSelection = hasSelection,
                    onSave = onSave,
                    onCancel = onCancel,
                    onDelete = onDelete
                )
            } else {
                IconTextButton(
                    icon = Icons.Outlined.Edit,
                    iconDesc = "edit icon",
                    text = "Edit parking",
                    onClick = onEdit
                )
            }
        } else {
            IconTextButton(
                icon = Icons.Outlined.LocationOn,
                iconDesc = "location icon",
                text = "Add parking spot",
                onClick = onEdit
            )
        }
    }
}

@Composable
fun EditingLocationButtons(
    hasPosition: Boolean,
    hasSelection: Boolean,
    onSave: Callback,
    onCancel: Callback,
    onDelete: Callback,
) {
    if (hasPosition) {
        IconTextButton(
            icon = Icons.Outlined.Delete,
            iconDesc = "delete icon",
            text = "Delete",
            onClick = onDelete
        )
    }

    IconTextButton(
        icon = Icons.Outlined.Close,
        iconDesc = "cancel icon",
        text = "Cancel",
        onClick = onCancel
    )

    IconTextButton(
        icon = Icons.Outlined.Check,
        iconDesc = "save icon",
        text = "Save",
        enabled = hasSelection,
        onClick = onSave
    )
}
