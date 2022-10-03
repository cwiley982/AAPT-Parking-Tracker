package com.caitlynwiley.aapparkingsaver

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class Prefs {
    companion object {
        private const val PREFS_FILE_NAME = "default_aap_prefs"
        private const val PARKING_DECK_LEVEL = "saved_parking_level"
        private const val LAST_SAVED_TIMESTAMP = "level_last_saved_ts"

        val DEFAULT_PREFS_IMPL = Prefs()
    }

    private lateinit var preferences: SharedPreferences
    private val changeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, s: String ->
            when (s) {
                PARKING_DECK_LEVEL -> _parkingLevel.value = preferences.getInt(PARKING_DECK_LEVEL, -1)
                LAST_SAVED_TIMESTAMP -> _lastUpdatedTimestamp.value = preferences.getLong(LAST_SAVED_TIMESTAMP, -1)
            }
        }

    constructor(context: Context) : this() {
        mutableStateOf(2)
        preferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        preferences.registerOnSharedPreferenceChangeListener(changeListener)

        initTrackedPrefs()
    }

    private constructor()

    private lateinit var _parkingLevel: MutableState<Int>
    var parkingLevel: Int
        get() = _parkingLevel.value
        set(value) {
            setInt(PARKING_DECK_LEVEL, value)
            setLong(LAST_SAVED_TIMESTAMP, System.currentTimeMillis())
        }

    private lateinit var _lastUpdatedTimestamp: MutableState<Long>
    var lastUpdatedTimestamp: Long = -1
        get() = _lastUpdatedTimestamp.value
        private set

    fun clearSavedLevel() {
        preferences.edit().remove(PARKING_DECK_LEVEL).apply()
    }

    private fun initTrackedPrefs() {
        _parkingLevel = mutableStateOf(preferences.getInt(PARKING_DECK_LEVEL, -1))
        _lastUpdatedTimestamp = mutableStateOf(preferences.getLong(LAST_SAVED_TIMESTAMP, -1))
    }

    private fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    private fun setLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }
}