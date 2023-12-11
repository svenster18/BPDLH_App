package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.toRupiah
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.databinding.ItemPermohonanPembiayaanBinding
import id.bpdlh.fdb.core.R as CoreR

class PembiayaanPerhutananItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemPermohonanPembiayaanBinding.inflate(
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

    fun bind(data: PembiayaanPerhutananEntity, onClicked: (PembiayaanPerhutananEntity) -> Unit) {
        layout.apply {
            tvDate.text = data.date
            badgeItem.setMessage(setBadgeText(data.status))
            badgeItem.setBadgeType(data.status)
            if (data.serviceType != 0) {
                badgeJenisLayanan.setMessage(setBadgeText(data.serviceType))
                badgeJenisLayanan.setBadgeType(data.serviceType)
            } else {
                tvJenisLayananHint.gone()
                badgeJenisLayanan.gone()
            }
            if (data.amountOfLoan <= 0) {
                tvJenisLayananHint.gone()
                badgeJenisLayanan.gone()
                tvTanggalPermohonanHint.gone()
                tvTanggalPermohonan.gone()
                tvNilaiPermohonanHint.gone()
                tvNilaiPermohonan.gone()
            } else {
                btnDataItem.visible()
                tvTanggalPermohonan.text = data.requestDate
            }
            tvNilaiPermohonan.text = data.amountOfLoan.toRupiah()
            btnDataItem.setOnClickListener {
                onClicked(data)
            }
            btnDraft.setOnClickListener {
                onClicked(data)
            }
        }
    }

    private fun setBadgeText(type: Int): String {
        return when (type) {
            FdbBadge.DRAFT -> {
                layout.btnDraft.visible()
                layout.btnDataItem.gone()
                layout.btnDraft.text =
                    context.getString(R.string.lanjutkan_permohonan)
                layout.btnDraft.setTextAppearance(CoreR.style.TextSM_SemiBold_2)
                context.getString(R.string.tab_draft)
            }

            FdbBadge.ON_REVIEW -> {
                layout.btnDataItem.setTextAppearance(CoreR.style.TextSM_SemiBold)
                context.getString(R.string.tab_menunggu_review)
            }

            FdbBadge.APPROVED -> {
                layout.btnDataItem.setTextAppearance(CoreR.style.TextSM_SemiBold)
                context.getString(R.string.tab_disetujui)
            }

            FdbBadge.REJECTED -> {
                layout.btnDataItem.setTextAppearance(CoreR.style.TextSM_SemiBold)
                context.getString(R.string.tab_ditolak)
            }

            FdbBadge.TUNDA_TEBANG -> context.getString(R.string.tunda_tebang)
            FdbBadge.REFINANCING_KEHUTANAN -> context.getString(R.string.refinancing_kehutanan)
            FdbBadge.HHBK -> context.getString(R.string.hhbk)

            else -> {
                context.getString(R.string.tab_draft)
            }
        }
    }

    companion object {
        fun toViewItem(
            data: PembiayaanPerhutananEntity,
            onClicked: (PembiayaanPerhutananEntity) -> Unit
        ): ViewItem<PembiayaanPerhutananItem> {
            return ViewItem(
                builder = {
                    PembiayaanPerhutananItem(it)
                },
                binder = {
                    it.bind(data, onClicked)
                },
                type = PembiayaanPerhutananItem::class.hashCode()
            )
        }
    }
}