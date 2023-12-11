package id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.databinding.ItemDaftarPermohonanBinding

class PengajuanDaftarPermohonanNonSosialItem @JvmOverloads constructor(
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
    fun bind(data: GroupApplicationEntity, onClicked: (String) -> Unit, onDetailClicked: (String) -> Unit, onDraftClicked: (String) -> Unit) {
        layout.apply {
            tvDate.text = data.createdAt?.convertStringDate(Constants.ISO_DATE_FORMAT, Constants.DATE_FORMAT_DD_MM_YYYY)
            badgeItem.setMessage(setBadgeText(data.status))
            badgeItem.setBadgeType(setBadgeType(data.status))
            tvTotalAnggotaDiajukan.text = context.getString(R.string.total_anggota_diajukan_value, data.totalMember.toString())
            btnDataItem.setOnClickListener {
                onClicked("")
            }
            btnDraft.setOnClickListener {
                data._id?.let { it1 -> onDraftClicked(it1) }
            }
            btnLihatDetail.setOnClickListener {
                data._id?.let { it1 -> onDetailClicked(it1) }
            }
            setButton(data.status)
        }
    }

    private fun setButton(type: String?) {
        with(layout) {
            when(type) {
                Constants.STATUS_ON_PROGRESS -> {
                    btnDraft.gone()
                    btnLihatDetail.visible()
                }
                Constants.STATUS_MENUNGGU_VERIFIKASI -> {
                    btnDraft.gone()
                    btnLihatDetail.visible()
                }
                else -> {
                    context.getString(R.string.tab_draft)
                }
            }

        }
    }

    private fun setBadgeText(type: String?) : String {
        return when(type) {
            Constants.STATUS_ON_PROGRESS -> {
                layout.btnDataItem.gone()
                layout.btnDraft.visible()
                return type
            }
            Constants.STATUS_MENUNGGU_VERIFIKASI -> {
                layout.btnDataItem.gone()
                layout.btnDraft.visible()
                return type
            }
            else -> {
                context.getString(R.string.tab_draft)
            }
        }
    }

    private fun setBadgeType(type: String?) : Int {
        return when(type) {
            Constants.STATUS_MENUNGGU_VERIFIKASI -> {
                FdbBadge.WAITING
            }
            Constants.STATUS_ON_PROGRESS -> {
                FdbBadge.ON_PROGRESS
            }
            else -> {
                0
            }
        }
    }

    companion object {
        fun toViewItem(
            data: GroupApplicationEntity,
            onClicked: (String) -> Unit,
            onDetailClicked: (String) -> Unit,
            onDraftClicked: (String) -> Unit
        ): ViewItem<PengajuanDaftarPermohonanNonSosialItem> {
            return ViewItem(
                builder = {
                    PengajuanDaftarPermohonanNonSosialItem(it)
                },
                binder = {
                    it.bind(data, onClicked,onDetailClicked, onDraftClicked)
                },
                type = PengajuanDaftarPermohonanNonSosialItem::class.hashCode()
            )
        }
    }
}