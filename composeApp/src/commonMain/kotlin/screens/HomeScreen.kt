package screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.ParkingModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.AppTheme
import ui.LevelPicker
import ui.Level
import ui.NoLevelSaved
import ui.isTimestampFromToday


object HomeScreen : Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {

        val model = rememberScreenModel { ParkingModel() }
        var showNoLevelSavedMsg by remember {
            mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour >= 15 )
        }

        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {

                val log = Logger.withTag("HomeScreen")
                log.d("parkingLevel -> ${model.parkingLevel.value}")
                log.d("lastUpdatedTimestamp -> ${model.lastUpdatedTimestamp.value}")
                log.d("x -> ${model.carX.value}, y ${model.carY.value}")
                log.d("editing -> ${model.inEditMode.value}")
                log.d("showMap -> ${model.showMap.value}")
                log.d("showNoLevelSavedMsg -> $showNoLevelSavedMsg")

                if (isTimestampFromToday(model.lastUpdatedTimestamp.value) && model.parkingLevel.value in (2..9)) {
                    Level(
                        level = model.parkingLevel.value,
                        showMap = model.showMap.value,
                        onClear = model::clearSavedLevel,
                        x = model.carX.value,
                        y = model.carY.value,
                        editing = model.inEditMode.value,
                        hasPosition = model.hasCarPosition.value,
                        hasSelection = model.parkingSpotWasSelected.value,
                        onPosition = model::updateTempCarPosition,
                        onEdit = {
                            model.setEditMode(true)
                        },
                        onSave = model::saveCarPosition,
                        onCancel = model::cancelEditing,
                        onDelete = model::deleteCarPosition
                    )
                } else {
                    if (showNoLevelSavedMsg) { // if it's after 3pm
                        NoLevelSaved(onDismiss = {
                            showNoLevelSavedMsg = false
                        })
                    } else {
                        LevelPicker(
                            onPick = model::updateParkingLevel
                        )
                    }
                }
            }
        }
    }
}