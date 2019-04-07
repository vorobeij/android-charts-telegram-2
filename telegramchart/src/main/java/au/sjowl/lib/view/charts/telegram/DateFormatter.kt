package au.sjowl.lib.view.charts.telegram

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale

object DateFormatter {
    private val calendar = GregorianCalendar()
    @SuppressLint("ConstantLocale")
    private val locale = Locale.getDefault()
    private val dateFormat = SimpleDateFormat("MMM d", locale)
    private val dateFormatLong = SimpleDateFormat("EEE, MMM d", locale)
    private val dateFormatInterval = SimpleDateFormat("dd MMMM yyyy", locale)
    private val dateFormatIntervalDay = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)

    fun format(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormat.format(calendar.time)
    }

    fun formatLong(timeInMillisec: Long): String {
        calendar.timeInMillis = timeInMillisec
        return dateFormatLong.format(calendar.time)
    }

    fun intervalFormat(start: Long, end: Long): String {
        calendar.timeInMillis = start

        val dateStart = dateFormatInterval.format(calendar.time)
        calendar.timeInMillis = end
        val dateEnd = dateFormatInterval.format(calendar.time)
        return if (dateStart != dateEnd)
            "$dateStart - $dateEnd"
        else
            dateFormatIntervalDay.format(calendar.time)
    }
}