package com.caitlynwiley.aapparkingsaver.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caitlynwiley.aapparkingsaver.Prefs
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.CAR_POSITION_SAVED_TS
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.LEVEL_SAVED_TS
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.PARKING_DECK_LEVEL
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.SAVED_CAR_LOCATION_X
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.SAVED_CAR_LOCATION_Y
import com.caitlynwiley.aapparkingsaver.ui.isTimestampFromToday
import com.caitlynwiley.aapparkingsaver.x
import com.caitlynwiley.aapparkingsaver.y

class ParkingViewModel(private val prefs: Prefs): ViewModel() {
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

    private val _parkingLevel = mutableStateOf(prefs.getInt(PARKING_DECK_LEVEL, -1))
    val parkingLevel: State<Int> = _parkingLevel

    private val _lastUpdatedTimestamp = mutableStateOf( prefs.getLong(LEVEL_SAVED_TS, -1L))
    val lastUpdatedTimestamp: State<Long> = _lastUpdatedTimestamp

    /*
    *  Stored car position, relative to the top left point of the floor plan image. Values are stored
    *  in dp, not px. By storing values in dp, (and since all stored data is on-device and not synced
    *  across devices,) this avoids potential issues moving between screens with different densities,
    *  since dp is density independent.
    */
    private val _carPosition = mutableStateOf(Pair(
            prefs.getFloat(SAVED_CAR_LOCATION_X, -1f),
            prefs.getFloat(SAVED_CAR_LOCATION_Y, -1f)))

    private val _carPositionSavedTime = mutableStateOf(prefs.getLong(CAR_POSITION_SAVED_TS, -1L))

    init {
        // set these here to avoid duplicate reads from prefs at startup
        _carX.value = _carPosition.value.x()
        _carY.value = _carPosition.value.y()
        _hasCarPosition.value = isTimestampFromToday(_carPositionSavedTime.value)

        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun setEditMode(editing: Boolean) {
        _inEditMode.value = editing
        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun updateParkingLevel(newLevel: Int) {
        val now = System.currentTimeMillis()
        _parkingLevel.value = newLevel
        _lastUpdatedTimestamp.value = now

        prefs.setInt(PARKING_DECK_LEVEL, newLevel)
        prefs.setLong(LEVEL_SAVED_TS, now)
    }

    fun clearSavedLevel() {
        _parkingLevel.value = -1
        prefs.remove(PARKING_DECK_LEVEL)

        deleteCarPosition()
    }

    fun saveCarPosition() {
        val now = System.currentTimeMillis()
        _inEditMode.value = false
        _carPosition.value = Pair(_carX.value, _carY.value)
        _carPositionSavedTime.value = now
        _hasCarPosition.value = true
        _parkingSpotWasSelected.value = false
        _showMap.value = _inEditMode.value || _hasCarPosition.value

        prefs.setFloat(SAVED_CAR_LOCATION_X, _carX.value)
        prefs.setFloat(SAVED_CAR_LOCATION_Y, _carY.value)
        prefs.setLong(CAR_POSITION_SAVED_TS, now)
    }

    fun cancelEditing() {
        _inEditMode.value = false
        _carX.value = _carPosition.value.x()
        _carY.value = _carPosition.value.y()
        _parkingSpotWasSelected.value = false
        _showMap.value = _inEditMode.value || _hasCarPosition.value
    }

    fun updateTempCarPosition(x: Float, y: Float, density: Float) {
        if (!_inEditMode.value) return

        _carX.value = x / density
        _carY.value = y / density
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

        prefs.remove(SAVED_CAR_LOCATION_X)
        prefs.remove(SAVED_CAR_LOCATION_Y)
        prefs.remove(CAR_POSITION_SAVED_TS)
    }

    class Factory(private val p: Prefs): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ParkingViewModel(p) as T
        }
    }
}