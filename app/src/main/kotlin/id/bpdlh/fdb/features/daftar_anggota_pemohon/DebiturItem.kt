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
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.ItemDebiturBinding

/**
 * Created by hahn on 23/09/23.
 * Project: BPDLH App
 */
class DebiturItem @JvmOverloads constructor(
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
        data: DataDebiturEntity,
        onLengkapiClicked: () -> Unit,
        onEditClicked: () -> Unit,
        onRemoveClicked: () -> Unit,
        isSubmitPage: Boolean = false
    ) {
        with(layout) {
            tvName.text = data.nama
            tvNikValue.text = data.nik
            tvTanggalLahirValue.text = data.tanggalLahir?.convertStringDate(
                input = Constants.ISO_DATE_FORMAT,
                output = Constants.DATE_FORMAT
            )
            tvEmailValue.text = data.email
            tvNoTelpValue.text = data.noTelp
            if (isSubmitPage) {
                btnEdit.gone()
                btnTrash.gone()
                btnLengkapiData.visible()
            } else {
                btnLengkapiData.gone()
                btnEdit.visible()
                btnTrash.visible()
            }
            grpLayanan.gone()
            btnLengkapiData.setOnClickListener {
                onLengkapiClicked()
            }
            btnEdit.setOnClickListener {
                onEditClicked()
            }
            btnTrash.setOnClickListener {
                onRemoveClicked()
            }
        }
    }

    companion object {
        fun toViewItem(
            data: DataDebiturEntity,
            onLengkapiClicked: () -> Unit,
            onEditClicked: () -> Unit,
            onRemoveClicked: () -> Unit,
            isSubmitPage: Boolean = false
        ): ViewItem<DebiturItem> {
            return ViewItem(
                builder = {
                    DebiturItem(it)
                },
                binder = {
                    it.bind(data, onLengkapiClicked, onEditClicked, onRemoveClicked, isSubmitPage)
                },
                type = DebiturItem::class.hashCode()
            )
        }
    }
}