package ui

import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import theme.Level2Orange
import theme.Level3Blue
import theme.Level4Yellow
import theme.Level5Green
import theme.Level6Purple
import theme.Level7Red
import theme.Level8Blue
import theme.Level9Yellow

fun getBackgroundColor(level: Int): Color {
    return when (level) {
        2 -> Level2Orange
        3 -> Level3Blue
        4 -> Level4Yellow
        5 -> Level5Green
        6 -> Level6Purple
        7 -> Level7Red
        8 -> Level8Blue
        9 -> Level9Yellow
        else -> Color.LightGray
    }
}

fun isTimestampFromToday(timestamp: Long): Boolean {
    val now = Clock.System.now()
    val then = Instant.fromEpochMilliseconds(timestamp)

    Logger.d("now -> $now; then -> $then")

    val nowDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val thenDate = then.toLocalDateTime(TimeZone.currentSystemDefault()).date

    return nowDate == thenDate
}