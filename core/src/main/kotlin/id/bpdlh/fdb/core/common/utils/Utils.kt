package id.bpdlh.fdb.core.common.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import id.bpdlh.fdb.core.common.widget.FdbBadge
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun startFileChooser(launcher : ActivityResultLauncher<Intent>) {
    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT

    val mimeTypes = arrayOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/pdf"
    )
    intent.type = "*/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

    launcher.launch(intent)
}

fun startExcelFileChooser(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT

    val mimeTypes = arrayOf(
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    )
    intent.type = "*/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

    launcher.launch(intent)
}

fun startImageChooser(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent()
    intent.action = Intent.ACTION_GET_CONTENT
    intent.type = "*/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, "image/*")

    launcher.launch(intent)
}

fun uriToFile(selectedImg: Uri, context: Context): File? {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context, selectedImg)
    if (myFile != null) {
        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
    return null
}

fun createCustomTempFile(context: Context, uri: Uri): File? {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val filename = uri.getFileName(context)
    if (filename != null) {
        val format = "." + (filename.split(".")[1])
        return File.createTempFile(filename.removeSuffix(format), format, storageDir)
    }
    return null
}

fun isDateInvalid(date: String, format: String): Boolean {
    return try {
        val df = SimpleDateFormat(format, Locale.getDefault())
        df.isLenient = false
        df.parse(date)
        false
    } catch (e: ParseException) {
        true
    }
}

fun convertToIndonesianWords(number: Long): String {
    val units = arrayOf("", "ribu", "juta", "miliar", "triliun")
    val ones =
        arrayOf("", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan")

    fun convertThreeDigits(num: Int): String {
        val sb = StringBuilder()
        val hundred = num / 100
        val remainder = num % 100
        if (hundred > 0) {
            sb.append(if (hundred == 1) " seratus" else "${ones[hundred]} ratus ")
        }
        if (remainder > 0) {
            if (remainder == 10) {
                sb.append(" sepuluh")
            } else if (remainder == 11) {
                sb.append(" sebelas")
            } else if (remainder < 10) {
                sb.append(ones[remainder])
            } else if (remainder < 20) {
                sb.append("${ones[remainder % 10]} belas")
            } else {
                sb.append("${ones[remainder / 10]} puluh ${ones[remainder % 10]}")
            }
        }
        return sb.toString()
    }


    var num = number
    var level = 0
    val result = StringBuilder()

    if (number == 0L) {
        return "Nol"
    }

    while (num > 0) {
        val threeDigits = (num % 1000).toInt()
        if (threeDigits > 0) {
            val words = convertThreeDigits(threeDigits)
            result.insert(0, "$words ${units[level]} ")
        }
        num /= 1000
        level++
    }

    return result.toString().trim().lowercase().capitalizeWords()
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { string ->
    string.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun getCurrentDate(): String {
    val cal = Calendar.getInstance()
    val myFormat = Constants.NEW_DATE_FORMAT
    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
    return sdf.format(cal.time)
}

fun getDataType(input: String): Int {
    return when (input) {
        "Draft" -> FdbBadge.DRAFT
        "On Progress" -> FdbBadge.ON_PROGRESS
        "Menunggu Verifikasi" -> FdbBadge.ON_VERIFY
        "Terverifikasi" -> FdbBadge.SUCCESS
        else -> FdbBadge.DRAFT
    }
}