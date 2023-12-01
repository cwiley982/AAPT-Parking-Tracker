package model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.datetime.Clock
import ui.isTimestampFromToday

val settings = Settings()

const val PARKING_DECK_LEVEL = "saved_parking_level"
const val LEVEL_SAVED_TS = "deck_level_saved_ts"
const val SAVED_CAR_LOCATION_X = "saved_car_location_x"
const val SAVED_CAR_LOCATION_Y = "saved_car_location_y"
const val CAR_POSITION_SAVED_TS = "car_position_saved_ts"

class ParkingModel : ScreenModel {

    private val _inEditMode = mutableStateOf(false)
    val inEditMode: State<Boolean> = _inEditMode

    private val _carX = mutableStateOf(-1f)
    val carX: State<Float> = _carX

    private val _carY = mutableStateOf(-1f)
    val carY: State<Float> = _carY

    private val _showMap = mutableStateOf(false)
    val showMap: State<Boolean> = _showMap

    private val _hasCarPosition = mutableStateOf(false)
    val hasCarPosition: State<Boolean> = _hasCarPosition

    private val _parkingSpotWasSelected = mutableStateOf(false)
    val parkingSpotWasSelected: State<Boolean> = _parkingSpotWasSelected

    private val _parkingLevel = mutableStateOf(settings[PARKING_DECK_LEVEL, -1])
    val parkingLevel: State<Int> = _parkingLevel

    private val _lastUpdatedTimestamp = mutableStateOf(settings[LEVEL_SAVED_TS, -1L])
    val lastUpdatedTimestamp: State<Long> = _lastUpdatedTimestamp

    /*
     *  Stored car position, relative to the top left point of the floor plan image. Values are stored
     *  in dp, not px. By storing values in dp, (and since all stored data is on-device and not synced
     *  across devices,) this avoids potential issues moving between screens with different densities,
     *  since dp is density independent.
     */
    private val _carPosition = mutableStateOf(Pair(
        settings[SAVED_CAR_LOCATION_X, -1f],
        settings[SAVED_CAR_LOCATION_Y, -1f]
    ))

    private val _carPositionSavedTime = mutableStateOf(settings[CAR_POSITION_SAVED_TS, -1L])

    init {
        _carX.value = _carPosition.value.x()
        _carY.value = _carPosition.value.y()
        _hasCarPosition.value = _carX.value != -1F && _carY.value != -1F && isTimestampFromToday(_carPositionSavedTime.value)
        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun setEditMode(editing: Boolean) {
        _inEditMode.value = editing
        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun updateParkingLevel(newLevel: Int) {
        val now = Clock.System.now().toEpochMilliseconds()
        _parkingLevel.value = newLevel
        _lastUpdatedTimestamp.value = now

        settings[PARKING_DECK_LEVEL] = newLevel
        settings[LEVEL_SAVED_TS] = now
    }

    fun clearSavedLevel() {
        _parkingLevel.value = -1
        settings.remove(PARKING_DECK_LEVEL)
        deleteCarPosition()
    }

    fun saveCarPosition() {
        val now = Clock.System.now().toEpochMilliseconds()
        _inEditMode.value = false
        _carPosition.value = Pair(_carX.value, _carY.value)
        _carPositionSavedTime.value = now
        _hasCarPosition.value = true
        _parkingSpotWasSelected.value = false
        _showMap.value = _inEditMode.value || _hasCarPosition.value

        settings[SAVED_CAR_LOCATION_X] = _carX.value
        settings[SAVED_CAR_LOCATION_Y] = _carY.value
        settings[CAR_POSITION_SAVED_TS] = now
    }

    fun cancelEditing() {
        _inEditMode.value = false
        _carX.value = _carPosition.value.x()
        _carY.value = _carPosition.value.y()
        _parkingSpotWasSelected.value = false
        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun updateTempCarPosition(x: Float, y: Float) {
        if (!_inEditMode.value) return

        _carX.value = x
        _carY.value = y
        _parkingSpotWasSelected.value = true
    }

    fun deleteCarPosition() {
        _inEditMode.value = false
        _carX.value = -1f
        _carY.value = -1f
        _parkingSpotWasSelected.value = false
        _carPosition.value = Pair(-1f, -1f)
        _carPositionSavedTime.value = -1L
        _hasCarPosition.value = false
        _showMap.value = _inEditMode.value || _hasCarPosition.value

        settings.remove(SAVED_CAR_LOCATION_X)
        settings.remove(SAVED_CAR_LOCATION_Y)
        settings.remove(CAR_POSITION_SAVED_TS)
    }

}

fun Pair<Float, Float>.x(): Float {
    return this.first
}

fun Pair<Float, Float>.y(): Float {
    return this.second
}