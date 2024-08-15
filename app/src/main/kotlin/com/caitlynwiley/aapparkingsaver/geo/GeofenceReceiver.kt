package com.caitlynwiley.aapparkingsaver.geo

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.caitlynwiley.aapparkingsaver.Constants.AAPT_GEOFENCE_REQ_ID
import com.caitlynwiley.aapparkingsaver.Constants.DATE_FORMAT
import com.caitlynwiley.aapparkingsaver.Constants.LAUNCH_APP_REQ_CODE
import com.caitlynwiley.aapparkingsaver.Constants.REMINDER_NOTIFICATION_CHANNEL_ID
import com.caitlynwiley.aapparkingsaver.Constants.SAVE_PARKING_NOTIFICATION_ID
import com.caitlynwiley.aapparkingsaver.Constants.TIME_FORMAT
import com.caitlynwiley.aapparkingsaver.Prefs
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.LEVEL_SAVED_TS
import com.caitlynwiley.aapparkingsaver.Prefs.Companion.PARKING_DECK_LEVEL
import com.caitlynwiley.aapparkingsaver.R
import com.caitlynwiley.aapparkingsaver.ui.MainActivity
import com.caitlynwiley.aapparkingsaver.ui.isTimestampFromToday
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class GeofenceReceiver: BroadcastReceiver() {
    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("GeofenceReceiver", errorMessage)
            return
        }

        /*
        int GEOFENCE_TRANSITION_ENTER = 1;
        int GEOFENCE_TRANSITION_EXIT = 2;
        int GEOFENCE_TRANSITION_DWELL = 4;
         */
        // todo: write these transitions to a local db so I can have history for future features
        //  and just general debugging purposes
        val transitionType = geofencingEvent.geofenceTransition
        Toast.makeText(context, "got broadcast, type: $transitionType", Toast.LENGTH_LONG).show()
        println("transition type: $transitionType")

        val prefs = Prefs(context)
        var count = 0
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                // don't write event to db, just post reminder to save their parking location if
                // they haven't already

                if (isTimestampFromToday(prefs.getLong(LEVEL_SAVED_TS)) && prefs.getInt(PARKING_DECK_LEVEL) in 2..9) {
                    // parking spot already saved, nothing to do here
                    return
                }

                val openAppIntent = Intent.makeMainActivity(ComponentName(context, MainActivity::class.java))
                val pendingIntent = PendingIntent.getActivity(context, LAUNCH_APP_REQ_CODE, openAppIntent, PendingIntent.FLAG_IMMUTABLE)

                val nm = context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
                val n = Notification.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Reminder to save parking")
                    .setContentText("Don't forget to save your parking location for today!")
                    .setSmallIcon(R.drawable.ic_outline_car_24)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                nm.notify(SAVE_PARKING_NOTIFICATION_ID, n)
                return
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> count = -1
            Geofence.GEOFENCE_TRANSITION_ENTER -> count = 1
        }

        val triggeringGeofences = geofencingEvent.triggeringGeofences
        if (triggeringGeofences?.firstOrNull()?.requestId == AAPT_GEOFENCE_REQ_ID) {
            val db = Firebase.database
            val reference = db.getReference("aapt")
            val p = reference.push()
            println("key from pushed ref: ${p.key}")
            val date = Date()
            val dateString = SimpleDateFormat(DATE_FORMAT).format(date)
            val timeString = SimpleDateFormat(TIME_FORMAT).format(date)
            p.setValue(ParkingCounter(count, dateString, timeString))
        }
    }

    @Serializable
    data class ParkingCounter(
        val count: Int,
        val date: String,
        val time: String
    )
}