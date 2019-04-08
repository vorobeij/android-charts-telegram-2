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

        val dateStart = dateFormatDMY.format(calendar.time)
        calendar.timeInMillis = end
        val dateEnd = dateFormatDMY.format(calendar.time)
        return if (dateStart != dateEnd)
            "$dateStart - $dateEnd"
        else
            dateFormatEDMY.format(calendar.time)
    }
}