package com.caitlynwiley.aapparkingsaver.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caitlynwiley.aapparkingsaver.PermissionsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionsViewModel: ViewModel() {
    private val _hasLocationPerms = MutableStateFlow(false)
    val hasLocationPerms: StateFlow<Boolean> = _hasLocationPerms

    private val _hasBackgroundLocationPerm = MutableStateFlow(false)
    val hasBackgroundPerm: StateFlow<Boolean> = _hasBackgroundLocationPerm

    private val _requireLocationPermissions = MutableStateFlow(false)
    val requireLocationPermissions: StateFlow<Boolean> = _requireLocationPermissions

    init {
        viewModelScope.launch {
            PermissionsRepo.hasLocationPermissions.collect {
                _hasLocationPerms.emit(it)
            }
        }

        viewModelScope.launch {
            PermissionsRepo.hasBackgroundLocationPermission.collect {
                _hasBackgroundLocationPerm.emit(it)
            }
        }

        viewModelScope.launch {
            PermissionsRepo.requireLocationPermissions.collect {
                _requireLocationPermissions.emit(it)
            }
        }
    }
}