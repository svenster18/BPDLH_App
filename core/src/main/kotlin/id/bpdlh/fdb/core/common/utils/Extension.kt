package id.bpdlh.fdb.core.common.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.common.utils.Constants.BYTE_SIZE
import id.bpdlh.fdb.core.common.utils.Constants.WARNING
import id.bpdlh.fdb.core.databinding.FdbToastBinding
import id.bpdlh.fdb.core.databinding.FragmentUploadFileBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.NumberFormat
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToInt


// Toast Extension
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.showToast(message: String, type: Int = Constants.SUCCESS) {
    val binding = FdbToastBinding.inflate(LayoutInflater.from(this))
    val backgroundResource: Int
    var resourceId = 0
    var textColor: Int = ContextCompat.getColor(this, R.color.semantic_success_700)
    when (type) {
        Constants.SUCCESS -> {
            backgroundResource = R.drawable.ticker_bg_info
            resourceId = R.drawable.ic_check_circle
        }

        WARNING -> {
            backgroundResource = R.drawable.ticker_bg_warning
            resourceId = R.drawable.ic_alert_triangle
            textColor = ContextCompat.getColor(this, R.color.semantic_warning_700)
        }

        else -> backgroundResource = R.drawable.ticker_bg_info
    }
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.view = binding.root
    with(binding) {
        if (resourceId != 0) {
            ivIcon.visibility = View.VISIBLE
            ivIcon.setImageResource(resourceId)
        }
        llBackground.setBackgroundResource(backgroundResource)
        tvMessage.text = message
        tvMessage.visibility = View.VISIBLE
        tvMessage.setTextColor(textColor)
    }
    toast.show()
}

// Hide Keyboard Extension
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@Keep
fun Snackbar.error(): Snackbar {
    view.background = ContextCompat.getDrawable(context, R.drawable.bg_snackbar)
    val textView =
        view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    ViewCompat.setElevation(view, 6f)
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    return this
}

// View visibility Extension
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

// Image View Extension
fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    )
}

fun ImageView.load(drawableResource: Int) {
    Glide.with(context)
        .load(drawableResource)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

// BottomSheetDialog Extension
fun BottomSheetDialog.setupFullHeight(activity: Activity) {
    val bottomSheet =
        this.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as View
    val behavior = BottomSheetBehavior.from(bottomSheet)
    val layoutParams = bottomSheet.layoutParams
    val windowHeight = activity.getWindowHeight()
    if (layoutParams != null) {
        layoutParams.height = windowHeight
    }
    bottomSheet.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
}

fun ImageView.loadImageNoCache(url: String, requestOptions: RequestOptions?) {
    Glide.with(context)
        .load(url)
        .apply(requestOptions ?: RequestOptions())
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun Activity.getWindowHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}

fun RecyclerView.addItemDecorationWithoutLastDivider() {

    if (layoutManager !is LinearLayoutManager)
        return

    addItemDecoration(object :
        DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation) {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            if (parent.getChildAdapterPosition(view) == state.itemCount - 1)
                outRect.setEmpty()
            else
                super.getItemOffsets(outRect, view, parent, state)
        }
    })
}

fun Context.toDp(dp: Float) = dp * (this.resources
    .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun Bitmap.imageBitmapToBase64(): String {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 98, stream)
    val b = stream.toByteArray()
    return Base64.encodeToString(b, Base64.CRLF)
}

fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Long.toRupiah(): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(this).toString()
        .replace("Rp", "Rp ")
        .replace(",00", ",-")
}

fun String.cutStringWithSuffix(startIndex: Int, endIndex: Int, suffix: String): String {
    val result: String = if (this.length > endIndex) {
        StringBuilder().append(
            this.subSequence(startIndex, endIndex),
            suffix
        ).toString()
    } else {
        this
    }

    return result
}

fun String.getError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    else -> null
}

fun String.getGeneralError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length < 2 -> context.getString(R.string.label_less_than_two)
    this.all { !it.isLetter() } -> context.getString(R.string.label_special_blank)
    this.length > 50 -> context.getString(R.string.label_more_than_fifty)
    else -> null
}

fun String.getOptionalError(context: Context): String? = when {
    this.length < 2 -> context.getString(R.string.label_less_than_two)
    this.all { !it.isLetter() } -> context.getString(R.string.label_special_blank)
    this.length > 50 -> context.getString(R.string.label_more_than_fifty)
    else -> null
}

fun String.getNikError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length != 16 -> context.getString(R.string.label_nik_not_sixteen)
    else -> null
}

fun String.getNoKkError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length != 16 -> context.getString(R.string.label_no_kk_not_sixteen)
    else -> null
}

fun String.getAlamatError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length > 250 -> context.getString(R.string.label_more_than_250)
    else -> null
}

fun String.getDateError(context: Context, formatDate: String = Constants.NEW_DATE_FORMAT): String? =
    when {
        this.isEmpty() -> context.getString(R.string.label_empty)
        isDateInvalid(this, formatDate) -> context.getString(R.string.label_date_format, formatDate)
        else -> null
    }

fun String.getYearError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length > 4 -> context.getString(R.string.label_year_format)
    else -> null
}

fun String.getEmailError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    !Patterns.EMAIL_ADDRESS.matcher(this)
        .matches() -> context.getString(R.string.label_email_wrong_format)

    else -> null
}

fun String.getPasswordError(context: Context, confirmPass: String? = null): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length < 8 -> context.getString(R.string.label_pass_less_then_eight)
    !confirmPass.isNullOrEmpty() && this != confirmPass -> {
        context.getString(R.string.label_pass_not_same)
    }

    else -> null
}

fun String.getPhoneNumberError(context: Context): String? = when {
    this.isEmpty() -> context.getString(R.string.label_empty)
    this.length < 8 -> context.getString(R.string.label_phone_number_under)
    this.length > 15 -> context.getString(R.string.label_phone_number_upper)
    else -> null
}

fun String.extractNumber(): String {
    val pattern = Pattern.compile("\\d+") // Regular expression to match numbers
    val matcher = pattern.matcher(this)
    var number = ""
    while (matcher.find()) {
        number += matcher.group()
    }
    return if (this.contains("Rp")) number.removeSuffix("00").ifEmpty { "0" }
    else number.ifEmpty { "0" }
}

fun Uri.getMimeType(context: Context): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()

    // Get the file extension from the URI
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(this.toString())

    // Determine the MIME type using the file extension
    var mimeType = mimeTypeMap.getMimeTypeFromExtension(fileExtension)

    // If the MIME type is not found, query the content resolver
    if (mimeType == null) {
        val cursor = contentResolver.query(this, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex =
                cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE)
            mimeType = cursor.getString(columnIndex)
            cursor.close()
        }
    }

    return mimeType
}

fun Uri.getFileName(context: Context): String? {
    var fileName: String? = null
    val cursor = context.contentResolver.query(this, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = it.getString(displayNameIndex)
            }
        }
    }

    return fileName
}

fun Uri.getFileSize(context: Context): Long {
    val contentResolver: ContentResolver = context.contentResolver
    var fileSize: Long = 0

    try {
        val inputStream: InputStream? = contentResolver.openInputStream(this)

        if (inputStream != null) {
            fileSize = inputStream.available().toLong()
            inputStream.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return fileSize
}

fun Uri.isValid(context: Context): Boolean {
    val size = this.getFileSize(context)
    if (size >= Constants.MAX_SIZE) {
        context.showToast(context.getString(R.string.file_maksimal), WARNING)
    }
    return size < Constants.MAX_SIZE
}

fun FragmentUploadFileBinding.showPreviewImage(uri: Uri, context: Context, name: String? = null) {
    if (!uri.isValid(context)) return
    val mimeType = uri.getMimeType(context)
    if (mimeType != null && mimeType.contains("image")) {
        btnTrashFile.visible()
        clUploadFile.apply {
            root.isClickable = true
            ivFile.visible()
            ivFile.setImageURI(uri)
            ivUpload.gone()
            tvUpload.gone()
            tvFormat.gone()
            name?.let {
                tvNamaFilePreview.visible()
                tvNamaFilePreview.text = name
            }
        }
    } else {
        clUploadFile.gone()
        clPreviewFile.apply {
            visible()
            btnTrash.visible()
            root.isClickable = false
            tvNamaFile.text = uri.getFileName(context)
            tvSizeFile.text = uri.getFileSize(context).formatWithUnit()
            name?.let {
                tvNamaFilePreview.visible()
                tvNamaFilePreview.text = name
            }
        }
    }
}

fun FragmentUploadFileBinding.removePreviewImage() {
    clUploadFile.apply {
        visible()
        ivFile.gone()
        btnTrash.gone()
        btnTrashFile.gone()
        ivUpload.visible()
        tvUpload.visible()
        tvFormat.visible()
        root.isClickable = true
    }
    clPreviewFile.gone()
}

fun FragmentUploadFileBinding.showPreviewImage(
    filePath: String,
    context: Context,
    mime: String = "",
    name: String? = null
) {
    if (filePath.isNotEmpty()) {
        if (filePath.startsWith("https")) {
            this.apply {
                root.isClickable = true
                ivFile.visible()
                Glide.with(context)
                    .load(filePath) // URL Gambar
                    .into(ivFile)
                ivUpload.gone()
                tvUpload.gone()
                tvFormat.gone()
            }
        } else {
            val uri = Uri.fromFile(File(filePath))
            val mimeType = uri.getMimeType(context)
            if (mimeType != null && mimeType.contains("image")) {
                this.apply {
                    root.isClickable = true
                    btnTrashFile.visible()
                    name?.let {
                        tvFileName.visible()
                        tvFileName.text = name
                    }
                    ivFile.visible()
                    ivFile.setImageURI(uri)
                    ivUpload.gone()
                    tvUpload.gone()
                    tvFormat.gone()
                }
            } else {
                val fileName = filePath.substringAfterLast("Pictures/")
                clUploadFile.gone()
                btnTrashFile.visible()
                clPreviewFile.apply {
                    name?.let {
                        tvNamaFilePreview.visible()
                        tvNamaFilePreview.text = name
                        tvFileName.visible()
                        tvFileName.text = name
                    }
                    root.isClickable = false
                    visible()
                    tvNamaFile.text = fileName
                    tvSizeFile.text = uri.getFileSize(context).formatWithUnit()
                }
            }
        }
    }
}

fun Long.formatWithUnit(): String {
    var unit = "B"
    var size = this.toDouble()
    when {
        this >= BYTE_SIZE * BYTE_SIZE -> unit = "MB"
        this >= BYTE_SIZE -> unit = "KB"
    }
    while (size >= BYTE_SIZE) {
        size /= BYTE_SIZE
    }

    return "${size.formatDecimal()} $unit"
}

fun Double.formatDecimal(): String {
    return String.format("%.2f", this, Locale.US)
}

fun TextInputLayout.error(error: String?) {
    this.error = error
    if (error != null) {
        this.requestFocus()
    }
}

fun Boolean?.orFalse(): Boolean = this ?: false

fun String?.orDefault(default: String): String {
    if (this.isNullOrEmpty()) return default
    return this
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> getParcelableExtra(key) as T?
}

inline fun <reified T : Parcelable> Intent.parcelableArrayListExtra(key: String): List<T>? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayListExtra(key, T::class.java)
    else -> getParcelableArrayListExtra(key)
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> getParcelable(key) as T?
}

fun View.disableInput() {
    this.isClickable = false
    this.isFocusable = false
    this.isEnabled = false
    if (this is AutoCompleteTextView) {
        this.setAdapter(null)
    } else if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            child.disableInput()
        }
    }
}
