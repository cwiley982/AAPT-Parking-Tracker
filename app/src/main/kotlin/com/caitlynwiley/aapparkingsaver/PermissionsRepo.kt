package com.caitlynwiley.aapparkingsaver

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PermissionsRepo {
    private val _hasLocationPermissions = MutableStateFlow(false)
    val hasLocationPermissions: StateFlow<Boolean> = _hasLocationPermissions

    private val _hasBackgroundLocationPermission = MutableStateFlow(false)
    val hasBackgroundLocationPermission: StateFlow<Boolean> = _hasBackgroundLocationPermission

    fun setHasLocationPermissions(hasPermissions: Boolean) {
        _hasLocationPermissions.value = hasPermissions
    }

    fun setHasBackgroundLocationPermission(hasPermission: Boolean) {
        _hasBackgroundLocationPermission.value = hasPermission
    }

    fun recheckPermissions(context: Context) {
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            setHasLocationPermissions(true)
        }

        if (Build.VERSION.SDK_INT < 29) {
            setHasBackgroundLocationPermission(true)
        } else {
            setHasBackgroundLocationPermission(
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            )
        }

//        GeofenceManager.setupGeofence(context)
    }
}