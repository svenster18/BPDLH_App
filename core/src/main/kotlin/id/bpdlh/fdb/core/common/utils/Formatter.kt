package id.bpdlh.fdb.core.common.utils

import android.util.Base64
import android.widget.DatePicker
import org.json.JSONObject
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun currencyFormatter(number: Long, displayCurrency: Boolean): String {
    val localeID = Locale("in", "ID")
    return if (displayCurrency) {
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.format(number.toDouble()).toString()
    } else {
        val numberFormat = NumberFormat.getInstance(localeID)
        numberFormat.format(number.toDouble()).toString()
    }
}

fun getDaysAgo(daysAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -daysAgo)
    return calendar.time
}

fun String.convertISOTimeToDate(pattern: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.getDefault())
    val convertedDate: Date?
    var formattedDate = "-"
    try {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        convertedDate = sdf.parse(this.replaceAfter(".", "+0000").replace(".", ""))
        formattedDate =
            SimpleDateFormat(pattern, Locale("id", "ID")).format(convertedDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedDate
}

fun Date.convertDateToISOTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(this)
}

fun String.convertStringDate(input: String = Constants.NEW_DATE_FORMAT, output: String = Constants.NEW_DATE_FORMAT): String {
    return try {
        val inputDateFormat = SimpleDateFormat(input, Locale.getDefault())
        val outputDateFormat = SimpleDateFormat(output, Locale.getDefault())
        val date = inputDateFormat.parse(this)
        outputDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        outputDateFormat.format(date)
    } catch (e: Exception) {
        this
    }
}

fun Date.convertDate(format: String = Constants.DATE_FORMAT): String {
    var formattedDate: String? = null
    try {
        formattedDate = SimpleDateFormat(format, Locale("id", "ID")).format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (!formattedDate.isNullOrEmpty()) return formattedDate
    return ""
}

fun String.convertStringDate(): String {
    val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale("id", "ID"))
    val convertedDate: Date?
    var formattedDate: String? = null
    try {
        convertedDate = sdf.parse(this)
        formattedDate =
            SimpleDateFormat(Constants.DATE_FORMAT, Locale("id", "ID")).format(convertedDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (!formattedDate.isNullOrEmpty()) return formattedDate
    return ""
}

fun String.convertStringDateAPI(): String {
    val sdf = SimpleDateFormat(Constants.NEW_DATE_FORMAT, Locale("id", "ID"))
    val convertedDate: Date?
    var formattedDate: String? = null
    try {
        convertedDate = sdf.parse(this)
        formattedDate =
            SimpleDateFormat(Constants.DATE_FORMAT_API, Locale("id", "ID")).format(convertedDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (!formattedDate.isNullOrEmpty()) return formattedDate
    return ""
}

fun String.convertISODateMultipleFormat(pattern: String, convertUTC: Boolean = true): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val convertedDate: Date?
    var formattedDate = "-"
    try {
        if (convertUTC) {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
        }
        convertedDate = sdf.parse(this)
        formattedDate =
            SimpleDateFormat(pattern, Locale("id", "ID")).format(convertedDate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return formattedDate
}


fun DatePicker.getDate(): String {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, this.year)
    cal.set(Calendar.MONTH, this.month)
    cal.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
    val myFormat = Constants.DATE_FORMAT
    val sdf = SimpleDateFormat(myFormat, Locale("id", "ID"))
    return sdf.format(cal.time)
}

fun DatePicker.getNewDate(): String {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, this.year)
    cal.set(Calendar.MONTH, this.month)
    cal.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
    val myFormat = Constants.NEW_DATE_FORMAT
    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
    return sdf.format(cal.time)
}

fun DatePicker.getCustomFormatDate(formatDate: String): String {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, this.year)
    cal.set(Calendar.MONTH, this.month)
    cal.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
    val sdf = SimpleDateFormat(formatDate, Locale.getDefault())
    return sdf.format(cal.time)
}

fun Long.convertToDate(): String {
    val date = Date(this)
    val myFormat = Constants.DATE_FORMAT
    val sdf = SimpleDateFormat(myFormat, Locale("id", "ID"))
    return sdf.format(date)
}

fun Long.convertMillisToDate(): Date {
    return Date(this)
}

fun Long.getTimeFromEpoch(): String {
    return String.format(
        "%02d:%02d:%02d",
//        TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toDays(this)),
        (TimeUnit.MILLISECONDS.toHours(this) - TimeUnit.DAYS.toHours(
            TimeUnit.MILLISECONDS.toDays(
                this
            )
        )),
        (TimeUnit.MILLISECONDS.toMinutes(this) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this))),
        (TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
        ))
    )
}

fun String.decodeJWT() = try {
    val splits = this.split(".")
    val decodedBytes = Base64.decode(splits[1], Base64.URL_SAFE)
    JSONObject(String(decodedBytes, Charsets.UTF_8))
} catch (e: Exception) {
    Timber.e("Err", e.toString())
    JSONObject()
}

fun String.toPascalCase(): String {
    val words = this.split("\\s+".toRegex())
    val pascalCaseWords = words.map { word ->
        word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(
                Locale.getDefault()
            ) else char.toString()
        }
    }
    return pascalCaseWords.joinToString("")
}