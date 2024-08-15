package com.caitlynwiley.aapparkingsaver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.caitlynwiley.aapparkingsaver.geo.GeofenceManager
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class ParkingSaverApp: Application() {
    override fun onCreate() {
        super.onCreate()

        PermissionsRepo.recheckPermissions(this)

        GeofenceManager.init(this)

        val fbOptions = FirebaseOptions.Builder()
            .setApplicationId("parking-app")
            .setDatabaseUrl("https://relay-corp-parking-default-rtdb.firebaseio.com/")
            .build()
        FirebaseApp.initializeApp(this, fbOptions)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel(
            Constants.REMINDER_NOTIFICATION_CHANNEL_ID,
            "parking reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ))
    }
}