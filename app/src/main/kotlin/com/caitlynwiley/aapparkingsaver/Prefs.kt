package com.caitlynwiley.aapparkingsaver

import android.content.Context
import android.content.SharedPreferences

class Prefs {
    companion object {
        private const val PREFS_FILE_NAME = "default_aap_prefs"
        const val PARKING_DECK_LEVEL = "saved_parking_level"
        const val LEVEL_SAVED_TS = "deck_level_saved_ts"
        const val SAVED_CAR_LOCATION_X = "saved_car_location_x"
        const val SAVED_CAR_LOCATION_Y = "saved_car_location_y"
        const val CAR_POSITION_SAVED_TS = "car_position_saved_ts"
    }

    private lateinit var preferences: SharedPreferences

    constructor(context: Context) : this() {
        preferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    private constructor()

    fun getInt(key: String, default: Int = -1): Int {
        return preferences.getInt(key, default)
    }

    fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getLong(key: String, default: Long = -1L): Long {
        return preferences.getLong(key, default)
    }

    fun setLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getFloat(key: String, default: Float = -1f): Float {
        return preferences.getFloat(key, default)
    }

    fun setFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }
}

fun Pair<Float, Float>.x(): Float {
    return this.first
}

fun Pair<Float, Float>.y(): Float {
    return this.second
}