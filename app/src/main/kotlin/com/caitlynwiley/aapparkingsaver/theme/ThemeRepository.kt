package com.caitlynwiley.aapparkingsaver.theme

import com.caitlynwiley.aapparkingsaver.Prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ThemeRepository {
    private val _useSystemTheme = MutableStateFlow(false)
    val useSystemTheme: StateFlow<Boolean> = _useSystemTheme

    private val _useDarkMode = MutableStateFlow(false)
    val useDarkMode: StateFlow<Boolean> = _useDarkMode

    init {
        _useSystemTheme.value = Prefs.getBool("use_system_theme")
        _useDarkMode.value = Prefs.getBool("use_dark_mode")
    }

    fun setFollowSystemTheme(followSystem: Boolean) {
        Prefs.setBool("use_system_theme", followSystem)
        _useSystemTheme.value = followSystem
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        Prefs.setBool("use_dark_mode", enabled)
        _useDarkMode.value = enabled
    }
}