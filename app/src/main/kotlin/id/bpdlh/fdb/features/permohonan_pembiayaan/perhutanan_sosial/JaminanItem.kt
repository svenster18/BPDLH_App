package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.databinding.ItemTambahJaminanBinding

class JaminanItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemTambahJaminanBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        isFocusable = false
        isClickable = false

    }

    fun bind(
        jaminan: JaminanEntity,
        position: Int,
        onChangeClick: (JaminanEntity, Int) -> Unit,
        onDelete: (Int) -> Unit
    ) {
        layout.apply {
            tvBentukJaminan.text = jaminan.bentuk
            tvNilaiJaminan.text = currencyFormatter(jaminan.nilai, true)
            btnUbah.setOnClickListener { onChangeClick(jaminan, position) }
            btnDelete.setOnClickListener { onDelete(position) }
        }
    }

    companion object {
        fun toViewItem(
            jaminan: JaminanEntity,
            position: Int,
            onChange: (JaminanEntity, Int) -> Unit,
            onDelete: (Int) -> Unit
        ): ViewItem<JaminanItem> {
            return ViewItem(
                builder = {
                    JaminanItem(it)
                },
                binder = {
                    it.bind(jaminan, position, onChange, onDelete)
                },
                type = JaminanItem::class.hashCode()
            )
        }
    }
}