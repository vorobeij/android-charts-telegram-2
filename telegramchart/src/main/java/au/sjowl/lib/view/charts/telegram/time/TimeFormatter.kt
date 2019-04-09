package au.sjowl.lib.view.charts.telegram.time

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

abstract class TimeFormatter {

    abstract val dateFormat: SimpleDateFormat

    private val calendar = GregorianCalendar()

    private val h = 3600_000

    fun format(timeMillisec: Long): String {
        calendar.timeInMillis = round(timeMillisec)
        return dateFormat.format(calendar.time)
    }

    fun round(timeMillisec: Long): Long {
        return timeMillisec - timeMillisec % h
    }
}