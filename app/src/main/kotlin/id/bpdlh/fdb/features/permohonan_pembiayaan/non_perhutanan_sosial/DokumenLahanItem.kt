package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.databinding.FragmentUploadFileBinding

class DokumenLahanItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = FragmentUploadFileBinding.inflate(
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
        fileEntity: FileEntity,
        position: Int,
        onChangeClick: (FileEntity, Int) -> Unit,
        onDelete: (Int) -> Unit
    ) {
        layout.apply {
            showPreviewImage(fileEntity.path, context, name = fileEntity.nama)
            root.setOnClickListener {
                onChangeClick(fileEntity, position)
            }
            clUploadFile.setOnClickListener {
                onChangeClick(fileEntity, position)
            }
            tvGantiData.setOnClickListener {
                onChangeClick(fileEntity, position)
            }
            btnTrash.setOnClickListener {
                onDelete(position)
            }
            btnTrashFile.setOnClickListener {
                onDelete(position)
            }
        }
    }

    companion object {
        fun toViewItem(
            fileEntity: FileEntity,
            position: Int,
            onChange: (FileEntity, Int) -> Unit,
            onDelete: (Int) -> Unit
        ): ViewItem<DokumenLahanItem> {
            return ViewItem(
                builder = {
                    DokumenLahanItem(it)
                },
                binder = {
                    it.bind(fileEntity, position, onChange, onDelete)
                },
                type = DokumenLahanItem::class.hashCode()
            )
        }
    }
}