package id.bpdlh.fdb.features.pengajuan_daftar_permohonan

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.databinding.ItemDaftarPermohonanBinding

/**
 * Created by hahn on 18/09/23.
 * Project: BPDLH App
 */
class DaftarPermohonanItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemDaftarPermohonanBinding.inflate(
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
    fun bind(data: DaftarPemohonEntity, onClicked: () -> Unit, onDraftClicked: (String) -> Unit) {
        layout.apply {
            tvDate.text = data.date
            badgeItem.setMessage(setBadgeText(data.dataType))
            badgeItem.setBadgeType(data.dataType)
            tvTotalAnggotaDiajukan.text = context.getString(R.string.total_anggota_diajukan_value, data.totalAnggota.toString())
            btnDataItem.setOnClickListener {
                onClicked()
            }
            btnDraft.setOnClickListener {
                onDraftClicked(data.id)
            }
        }
    }

    private fun setBadgeText(type: Int) : String {
        return when(type) {
            FdbBadge.DRAFT -> {
                layout.btnDataItem.gone()
                layout.btnDraft.visible()
                context.getString(R.string.tab_draft)
            }
            FdbBadge.ON_PROGRESS -> {
                context.getString(R.string.tab_on_progress)
            }
            FdbBadge.ON_VERIFY -> {
                context.getString(R.string.tab_menunggu_verifikasi)
            }
            FdbBadge.VERIFIED -> {
                context.getString(R.string.tab_terverifikasi)
            }
            else -> {
                context.getString(R.string.tab_draft)
            }
        }
    }

    companion object {
        fun toViewItem(
            data: DaftarPemohonEntity,
            onClicked: () -> Unit,
            onDraftClicked: (String) -> Unit
        ): ViewItem<DaftarPermohonanItem> {
            return ViewItem(
                builder = {
                    DaftarPermohonanItem(it)
                },
                binder = {
                    it.bind(data, onClicked, onDraftClicked)
                },
                type = DaftarPermohonanItem::class.hashCode()
            )
        }
    }
}