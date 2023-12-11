package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.getOptionalError
import id.bpdlh.fdb.databinding.ItemMitraUsahaBinding


class MitraItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), TextWatcherTextChange {

    private val layout = ItemMitraUsahaBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bind(
        mitra: String,
        position: Int,
        onChange: (String, Int) -> Unit,
        onDelete: (Int) -> Unit
    ) {
        layout.apply {
            edtMitraUsaha.setText(mitra)
            textILMitraUsaha1.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(
                    mitraCharSequence: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    onChange(mitraCharSequence.toString(), position)
                }

                override fun afterTextChanged(p0: Editable?) {}

            })
            btnDelete1.setOnClickListener { onDelete(position) }
        }
    }

    companion object {
        fun toViewItem(
            mitra: String,
            position: Int,
            onChange: (String, Int) -> Unit,
            onDelete: (Int) -> Unit
        ): ViewItem<MitraItem> {
            return ViewItem(
                builder = {
                    MitraItem(it)
                },
                binder = {
                    it.bind(mitra, position, onChange, onDelete)
                },
                type = MitraItem::class.hashCode()
            )
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        layout.apply {
            when (editText) {
                textILMitraUsaha1.editText -> {
                    val strMitraUsaha = editText?.text.toString()
                    textILMitraUsaha1.error = strMitraUsaha.getOptionalError(context)
                }
            }
        }
    }
}