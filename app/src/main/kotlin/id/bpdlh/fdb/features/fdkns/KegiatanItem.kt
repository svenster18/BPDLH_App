package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.databinding.ItemKegiatanBinding

class KegiatanItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemKegiatanBinding.inflate(
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
    fun bind(data: ActivityEntity, type: String, onEdit: () -> Unit, onDelete: () -> Unit) {
        layout.apply {
            when(type) {
                Constants.GAMBARAN_UMUM,
                Constants.KEGIATAN_KELOMPOK -> {
                    tvKategoriUsaha.gone()
                    tvKategoriUsahaValue.gone()
                }
                Constants.MITRA_USAHA -> {
                    tvKategori.gone()
                    tvKategoriValue.gone()
                    tvDeskripsi.gone()
                    tvDeskripsiValue.gone()
                }
            }
            tvKategoriValue.text = data.category
            tvDeskripsiValue.text = data.description
            tvKategoriUsahaValue.text = data.category
            btnDataItem.setOnClickListener {
                onEdit()
            }
            btnTrash.setOnClickListener {
                onDelete()
            }
        }
    }

    companion object {
        fun toViewItem(
            data: ActivityEntity,
            type: String,
            onEdit: () -> Unit,
            onDelete: () -> Unit,
        ): ViewItem<KegiatanItem> {
            return ViewItem(
                builder = {
                    KegiatanItem(it)
                },
                binder = {
                    it.bind(data, type, onEdit, onDelete)
                },
                type = KegiatanItem::class.hashCode()
            )
        }
    }
}