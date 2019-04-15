package au.sjowl.lib.view.charts.telegram.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

object DateFormatter {
    private val calendar = GregorianCalendar()
    @SuppressLint("ConstantLocale")
    private val locale = Locale.getDefault()
    private val dateFormatMD = SimpleDateFormat("MMM d", locale)
    private val dateFormatDMY = SimpleDateFormat("dd MMMM yyyy", locale)
    private val dateFormatEDMY = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)
    private val dateFormatYMShort = SimpleDateFormat("yyyy-MM", locale)
    private val dateFormatDShort = SimpleDateFormat("dd", locale)

    fun formatYMShort(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatYMShort.format(calendar.time)
    }

    fun formatDShort(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatDShort.format(calendar.time)
    }

    fun format(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatMD.format(calendar.time)
    }

    fun getDayBorders(timeInMillisec: Long): DayBorders {
        calendar.timeInMillis = timeInMillisec
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        calendar.timeInMillis = timeInMillisec
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis
        return DayBorders(start, end)
    }

    fun intervalFormat(start: Long, end: Long): String {
        calendar.timeInMillis = start

        val ts = calendar.time
        val dateStart = dateFormatDMY.format(ts)

        calendar.timeInMillis = end
        val te = calendar.time
        val dateEnd = dateFormatDMY.format(te)

        val day = 86400000
        if (dateStart != dateEnd && end == start + day && ts.hours == 0) {
            return dateFormatEDMY.format(ts)
        }

        return if (dateStart != dateEnd)
            "$dateStart - $dateEnd"
        else
            dateFormatEDMY.format(calendar.time)
    }
}

data class DayBorders(
    val start: Long,
    val end: Long
)