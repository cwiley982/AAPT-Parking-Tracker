package com.caitlynwiley.aapparkingsaver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.aapparkingsaver.ui.home.HomeScreen
import com.caitlynwiley.aapparkingsaver.ui.settings.SettingsScreen

@Composable
fun BaseScreen() {
    Column {
        val navHost = rememberNavController()
        NavHost(modifier = Modifier.weight(1f), navController = navHost, startDestination = "home") {
            composable("home") {
                HomeScreen()
            }

            composable("settings") {
                SettingsScreen()
            }

            composable("history") {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.wrapContentSize().align(Alignment.Center),
                        text = "Coming soon!",
                        fontSize = 36.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Button(modifier = Modifier.weight(1f), onClick = {
                navHost.navigate("history")
            }) {
                Icon(
                    modifier = Modifier.padding(vertical = 8.dp).size(32.dp),
                    imageVector = Icons.Default.List,
                    contentDescription = "history"
                )
            }

            Button(modifier = Modifier.weight(1f), onClick = {
                navHost.navigate("home")
            }) {
                Icon(
                    modifier = Modifier.padding(vertical = 8.dp).size(32.dp),
                    imageVector = Icons.Default.Home,
                    contentDescription = "home"
                )
            }

            Button(modifier = Modifier.weight(1f), onClick = {
                navHost.navigate("settings")
            }) {
                Icon(
                    modifier = Modifier.padding(vertical = 8.dp).size(32.dp),
                    imageVector = Icons.Default.Settings,
                    contentDescription = "settings"
                )
            }
        }
    }
}