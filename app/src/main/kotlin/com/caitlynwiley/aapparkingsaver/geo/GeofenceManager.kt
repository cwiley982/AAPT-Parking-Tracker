package com.caitlynwiley.aapparkingsaver.geo

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.caitlynwiley.aapparkingsaver.Constants
import com.caitlynwiley.aapparkingsaver.Constants.BROADCAST_GEOFENCE_REQ_CODE
import com.caitlynwiley.aapparkingsaver.PermissionsRepo
import com.caitlynwiley.aapparkingsaver.Prefs
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object GeofenceManager {
    private lateinit var geofencingClient: GeofencingClient

    private const val ALL_TRANSITION_TYPES = Geofence.GEOFENCE_TRANSITION_ENTER or
                                             Geofence.GEOFENCE_TRANSITION_DWELL or
                                             Geofence.GEOFENCE_TRANSITION_EXIT

    private lateinit var prefs: Prefs

    @SuppressLint("MissingPermission")
    fun init(context: Context) {
        geofencingClient = LocationServices.getGeofencingClient(context)
        prefs = Prefs(context)
        CoroutineScope(Dispatchers.Default).launch {
            combine(
                PermissionsRepo.hasLocationPermissions,
                PermissionsRepo.hasBackgroundLocationPermission
            ) { hasLocation, hasBackground ->
                if (hasLocation && hasBackground) {
                    println("got all permissions, setting up geofence")
                    setupGeofence(context)
                }
            }.collect()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun setupGeofence(context: Context) {
        if (prefs.getBool("geofences_created")) {
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
                prefs.setBool("geofences_created", true)
            }
            addOnFailureListener {
                // Failed to add geofences
                println("failed to add geofence: ${it.message}")
            }
        }
    }

    private val aaptGeofence = Geofence.Builder()
        .setRequestId(Constants.AAPT_GEOFENCE_REQ_ID)
        .setCircularRegion(
            35.835923,
            -78.638841,
            60F // meters
        )
        .setLoiteringDelay(TimeUnit.MINUTES.toMillis(5).toInt())
        .setTransitionTypes(ALL_TRANSITION_TYPES)
        .setNotificationResponsiveness(30_000)
        .build()
}