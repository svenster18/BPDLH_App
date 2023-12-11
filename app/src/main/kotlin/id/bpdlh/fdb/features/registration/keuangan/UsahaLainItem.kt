package id.bpdlh.fdb.features.registration.keuangan

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.domain.entities.UsahaLainEntity
import id.bpdlh.fdb.databinding.ItemUsahaLainBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.JaminanItem

class UsahaLainItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemUsahaLainBinding.inflate(
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
        usahaLain: UsahaLainEntity,
        position: Int,
        onChangeClick: (UsahaLainEntity, Int) -> Unit
    ) {
        layout.apply {
            tvJenisUsahaValue.text = usahaLain.jenis
            tvPerkiraanPendapatanValue.text = currencyFormatter(usahaLain.perkiraanPendapatan, true)
            tvSiklusPendapatanValue.text = context.getString(
                R.string.siklus_pendapatan_placeholder,
                usahaLain.siklusPendapatan,
                usahaLain.satuanSiklusPendapatan
            )
            btnEdit.setOnClickListener { onChangeClick(usahaLain, position) }
        }
    }

    companion object {
        fun toViewItem(
            usahaLain: UsahaLainEntity,
            position: Int,
            onChange: (UsahaLainEntity, Int) -> Unit
        ): ViewItem<UsahaLainItem> {
            return ViewItem(
                builder = {
                    UsahaLainItem(it)
                },
                binder = {
                    it.bind(usahaLain, position, onChange)
                },
                type = JaminanItem::class.hashCode()
            )
        }
    }
}