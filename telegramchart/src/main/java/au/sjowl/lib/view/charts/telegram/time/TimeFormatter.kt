package au.sjowl.lib.view.charts.telegram.time

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

abstract class TimeFormatter {

    abstract val dateFormat: SimpleDateFormat

    private val calendar = GregorianCalendar()

    fun format(timeMillisec: Long): String {
        calendar.timeInMillis = timeMillisec
        return dateFormat.format(calendar.time)
    }
}