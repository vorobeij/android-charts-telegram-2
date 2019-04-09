package au.sjowl.lib.view.charts.telegram

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale

object DateFormatter {
    private val calendar = GregorianCalendar()
    @SuppressLint("ConstantLocale")
    private val locale = Locale.getDefault()
    private val dateFormatMD = SimpleDateFormat("MMM d", locale)
    private val dateFormatEMD = SimpleDateFormat("EEE, MMM d", locale)
    private val dateFormatDMY = SimpleDateFormat("dd MMMM yyyy", locale)
    private val dateFormatEDMY = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)
    private val dateFormatEDMYShort = SimpleDateFormat("EEE, dd MMM yyyy", locale)
    private val dateFormatYMShort = SimpleDateFormat("yyyy-MM", locale)
    private val dateFormatDShort = SimpleDateFormat("dd", locale)
    private val formatLong = SimpleDateFormat("d MMM yyyy HH:mm", locale)

    fun formatLongest(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return formatLong.format(calendar.time)
    }

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

    fun formatLong(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatEMD.format(calendar.time)
    }

    fun formatEDMYShort(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatEDMYShort.format(calendar.time)
    }

    fun formatEDMY(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatEDMY.format(calendar.time)
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