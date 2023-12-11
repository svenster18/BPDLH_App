package id.bpdlh.fdb.features.daftar_anggota_pemohon

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.ItemDetailDaftarAnggotaBinding

/**
 * Created by hahn on 25/09/23.
 * Project: BPDLH App
 */
class DetailDaftarAnggotaItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val layout = ItemDetailDaftarAnggotaBinding.inflate(
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

    fun bind(data: DataDebiturEntity) {
        with(layout) {
            tvName.text = data.nama
            tvNikValue.text = data.nik
            tvTanggalLahirValue.text = data.tanggalLahir?.convertStringDate(input = Constants.ISO_DATE_FORMAT,
                output = Constants.DATE_FORMAT)
            tvEmailValue.text = data.email
            tvNoTelpValue.text = data.noTelp
            tvTanggalDisetujui.text = data.tanggalDisetujui
            tvDisetujuiOleh.text = data.disetujuiOleh
        }
    }

    companion object {
        fun toViewItem(data: DataDebiturEntity): ViewItem<DetailDaftarAnggotaItem> {
            return ViewItem(
                builder = {
                    DetailDaftarAnggotaItem(it)
                },
                binder = {
                    it.bind(data)
                },
                type = DetailDaftarAnggotaItem::class.hashCode()
            )
        }
    }
}