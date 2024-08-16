package com.caitlynwiley.aapparkingsaver.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caitlynwiley.aapparkingsaver.theme.ThemeRepository

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val vm = viewModel<SettingsViewModel>()
        val useSystemTheme by vm.useSystemTheme.collectAsState()
        val darkModeEnabled by vm.useDarkMode.collectAsState()
        val useParkingReminders by vm.useParkingReminders.collectAsState()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Follow system theme",
                fontSize = 20.sp
            )
            Switch(checked = useSystemTheme, onCheckedChange = { ThemeRepository.setFollowSystemTheme(it) })
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Use dark theme",
                fontSize = 20.sp
            )
            Switch(checked = darkModeEnabled, onCheckedChange = { ThemeRepository.setDarkModeEnabled(it) }, enabled = !useSystemTheme)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Enable parking reminders",
                fontSize = 20.sp
            )
            Switch(checked = useParkingReminders, onCheckedChange = { vm.enableParkingReminders(it) })
        }
    }
}