package com.caitlynwiley.aapparkingsaver.geo

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.caitlynwiley.aapparkingsaver.Constants.AAPT_GEOFENCE_REQ_ID
import com.caitlynwiley.aapparkingsaver.Constants.BROADCAST_GEOFENCE_REQ_CODE
import com.caitlynwiley.aapparkingsaver.PermissionsRepo
import com.caitlynwiley.aapparkingsaver.Prefs
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object GeofenceManager {
    private lateinit var geofencingClient: GeofencingClient
    private var geofenceEnabled = MutableStateFlow(false)

    private const val ALL_TRANSITION_TYPES = Geofence.GEOFENCE_TRANSITION_ENTER or
                                             Geofence.GEOFENCE_TRANSITION_DWELL or
                                             Geofence.GEOFENCE_TRANSITION_EXIT

    @SuppressLint("MissingPermission")
    fun init(context: Context) {
        geofencingClient = LocationServices.getGeofencingClient(context)
        geofenceEnabled.value = Prefs.getBool("reminders_enabled")

        CoroutineScope(Dispatchers.Default).launch {
            combine(
                geofenceEnabled,
                PermissionsRepo.hasLocationPermissions,
                PermissionsRepo.hasBackgroundLocationPermission
            ) { geofenceEnabled, hasLocation, hasBackground ->
                if (geofenceEnabled && hasLocation && hasBackground) {
                    println("got all permissions, setting up geofence")
                    setupGeofence(context)
                }
            }.collect()
        }
    }

    fun setGeofenceEnabled(enabled: Boolean) {
        geofenceEnabled.value = enabled
        Prefs.setBool("reminders_enabled", enabled)

        if (!enabled) {
            removeGeofence()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun setupGeofence(context: Context) {
        if (Prefs.getBool("geofences_created")) {
            println("geofences already created, skipping this step")
            return
        }

        val geofenceRequest = GeofencingRequest.Builder()
            .addGeofence(aaptGeofence)
            .setInitialTrigger(ALL_TRANSITION_TYPES)
            .build()

        val intent = Intent(context, GeofenceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            BROADCAST_GEOFENCE_REQ_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        geofencingClient.addGeofences(geofenceRequest, pendingIntent).run {
            addOnSuccessListener {
                // Geofences added
                println("successfully added geofence")
                Prefs.setBool("geofences_created", true)
            }
            addOnFailureListener {
                // Failed to add geofences
                println("failed to add geofence: ${it.message}")
            }
        }
    }

    private fun removeGeofence() {
        geofencingClient.removeGeofences(listOf(AAPT_GEOFENCE_REQ_ID)).run {
            addOnSuccessListener {
                println("geofence removed")
                Prefs.setBool("geofences_created", false)
            }
            addOnFailureListener {
                println("unable to remove geofence")
            }
        }
    }

    private val aaptGeofence = Geofence.Builder()
        .setRequestId(AAPT_GEOFENCE_REQ_ID)
        .setCircularRegion(
            35.835923,
            -78.638841,
            60F // meters
        )
        .setLoiteringDelay(TimeUnit.MINUTES.toMillis(5).toInt())
        .setTransitionTypes(ALL_TRANSITION_TYPES)
        .setNotificationResponsiveness(30_000)
        .build()

    /*
    The app must re-register geofences if they're still needed after the following events,
    since the system cannot recover the geofences in the following cases:

    - The device is rebooted. The app should listen for the device's boot complete action,
      and then re- register the geofences required.
    - The app is uninstalled and re-installed.
    - The app's data is cleared.
    - Google Play services data is cleared.
    - The app has received a GEOFENCE_NOT_AVAILABLE alert. This typically happens after NLP
      (Android's Network Location Provider) is disabled.
     */
}