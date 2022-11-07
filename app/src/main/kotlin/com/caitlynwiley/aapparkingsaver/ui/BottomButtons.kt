package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.viewmodel.ParkingViewModel

@Composable
fun BottomButtonRow() {
    val vm = viewModel<ParkingViewModel>()
    val editing by vm.inEditMode
    val showMap by vm.showMap
    val arrangement = if (editing) Arrangement.SpaceAround else Arrangement.End

    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
        if (showMap) {
            if (editing) {
                EditingLocationButtons()
            } else {
                ViewingLocationButtons()
            }
        } else {
            IconTextButton(icon = Icons.Outlined.LocationOn, iconDesc = "location icon",
                text = "Add parking spot", onClick = { vm.setEditMode(true) })
        }
    }
}

@Composable
fun EditingLocationButtons() {
    val vm = viewModel<ParkingViewModel>()
    val hasPreviousCarPosition by vm.hasCarPosition
    val selectionWasMade by vm.parkingSpotWasSelected

    if (hasPreviousCarPosition) {
        IconTextButton(
            icon = Icons.Outlined.Delete, iconDesc = "delete icon",
            text = "Delete", onClick = vm::deleteCarPosition
        )
    }

    IconTextButton(icon = Icons.Outlined.Close, iconDesc = "cancel icon",
        text = "Cancel", onClick = vm::cancelEditing
    )

    IconTextButton(
        icon = Icons.Outlined.Check, iconDesc = "save icon",
        text = "Save", onClick = vm::saveCarPosition,
        enabled = selectionWasMade
    )
}

@Composable
fun ViewingLocationButtons() {
    val vm = viewModel<ParkingViewModel>()
    IconTextButton(icon = Icons.Outlined.Edit, iconDesc = "edit icon",
        text = "Edit parking", onClick = { vm.setEditMode(true) })
}