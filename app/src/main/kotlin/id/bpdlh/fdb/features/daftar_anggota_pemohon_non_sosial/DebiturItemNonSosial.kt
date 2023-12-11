package id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.invisible
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.databinding.ItemDebiturBinding

class DebiturItemNonSosial @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val layout = ItemDebiturBinding.inflate(
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
        data: DataDebiturNonSosialEntity,
        onLengkapiClicked: () -> Unit,
        onEditClicked: () -> Unit,
        onRemoveClicked: () -> Unit
    ) {
        with(layout) {
            tvName.text = data.nama
            tvNikValue.text = data.nik
            tvTanggalLahirValue.text = data.tanggalLahir.toString().convertISOTimeToDate(Constants.DATE_FORMAT)
            tvEmailValue.text = data.email
            tvNoTelpValue.text = data.noTelp
            badgeItem.setBadgeType(setBadgeType(data.jenisLayanan))
            badgeItem.setMessage(data.jenisLayanan)
            tvNilaiPermohonanValue.text =
                data.nilaiPermohonan?.toLong()?.let { currencyFormatter(it, true) }
            tvJenisLayanan.visible()
            tvNilaiPermohonan.visible()
            tvNilaiPermohonanValue.visible()

            btnLengkapiData.setOnClickListener {
                onLengkapiClicked()
            }
            btnEdit.setOnClickListener {
                onEditClicked()
            }
            btnTrash.setOnClickListener {
                onRemoveClicked()
            }
            data.isSend?.let {
                if (it){
                    btnEdit.gone()
                    btnTrash.gone()
                    btnLengkapiData.visible()
                } else {
                    btnEdit.visible()
                    btnTrash.visible()
                    btnLengkapiData.invisible()
                }
            }
        }
    }

    private fun setBadgeType(type: String?) : Int {
        return when(type) {
            Constants.TUNDA_TEBANG -> {
                FdbBadge.TUNDA_TEBANG
            }
            Constants.HASIL_HUTAN_BUKAN_KAYU -> {
                FdbBadge.HHBK
            }
            Constants.KOMODITAS_NON_KEHUTANAN -> {
                FdbBadge.REFINANCING_KEHUTANAN
            }
            else -> {
                0
            }
        }
    }

    companion object {
        fun toViewItem(
            data: DataDebiturNonSosialEntity,
            onLengkapiClicked: () -> Unit,
            onEditClicked: () -> Unit,
            onRemoveClicked: () -> Unit
        ): ViewItem<DebiturItemNonSosial> {
            return ViewItem(
                builder = {
                    DebiturItemNonSosial(it)
                },
                binder = {
                    it.bind(data, onLengkapiClicked, onEditClicked, onRemoveClicked)
                },
                type = DebiturItemNonSosial::class.hashCode()
            )
        }
    }
}