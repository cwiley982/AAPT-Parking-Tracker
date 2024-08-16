package com.caitlynwiley.aapparkingsaver.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caitlynwiley.aapparkingsaver.PermissionsRepo
import com.caitlynwiley.aapparkingsaver.Prefs
import com.caitlynwiley.aapparkingsaver.geo.GeofenceManager
import com.caitlynwiley.aapparkingsaver.theme.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val _useSystemTheme = MutableStateFlow(false)
    val useSystemTheme: StateFlow<Boolean> = _useSystemTheme

    private val _useDarkMode = MutableStateFlow(false)
    val useDarkMode: StateFlow<Boolean> = _useDarkMode

    private val _useParkingReminders = MutableStateFlow(false)
    val useParkingReminders: StateFlow<Boolean> = _useParkingReminders

    init {
        _useParkingReminders.value = Prefs.getBool("reminders_enabled")

        viewModelScope.launch {
            ThemeRepository.useSystemTheme.collect {
                _useSystemTheme.emit(it)
            }
        }

        viewModelScope.launch {
            ThemeRepository.useDarkMode.collect {
                _useDarkMode.emit(it)
            }
        }
    }

    fun enableParkingReminders(enabled: Boolean) {
        _useParkingReminders.value = enabled
        PermissionsRepo.setLocationPermissionsRequired(enabled)

        GeofenceManager.setGeofenceEnabled(enabled)
        // setting up geofence is taken care of in GeofenceManager by collecting the necessary flows

        // todo: also need to request permission to post notifications
    }
}