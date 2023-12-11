package id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.databinding.ItemDetailDaftarAnggotaBinding

class DetailDaftarAnggotaNonSosialItem @JvmOverloads constructor(
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

    fun bind(data: DataDebiturNonSosialEntity) {
        with(layout) {
            tvName.text = data.nama
            tvNikValue.text = data.nik
            tvTanggalLahirValue.text = data.tanggalLahir
            tvEmailValue.text = data.email
            tvNoTelpValue.text = data.noTelp
            tvTanggalDisetujui.text = data.tanggalDisetujui
            tvDisetujuiOleh.text = data.disetujuiOleh
            tvJenisLayananValue.text = data.jenisLayanan
            tvNilaiPermohonanValue.text =
                data.nilaiPermohonan?.toLong()?.let { currencyFormatter(it, true) }

            tvJenisLayanan.visible()
            tvJenisLayananValue.visible()
            tvNilaiPermohonan.visible()
            tvNilaiPermohonanValue.visible()
        }
    }

    companion object {
        fun toViewItem(data: DataDebiturNonSosialEntity): ViewItem<DetailDaftarAnggotaNonSosialItem> {
            return ViewItem(
                builder = {
                    DetailDaftarAnggotaNonSosialItem(it)
                },
                binder = {
                    it.bind(data)
                },
                type = DetailDaftarAnggotaNonSosialItem::class.hashCode()
            )
        }
    }
}