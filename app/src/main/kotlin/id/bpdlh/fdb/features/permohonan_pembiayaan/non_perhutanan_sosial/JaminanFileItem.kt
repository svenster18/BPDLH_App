package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.databinding.FragmentUploadFileBinding

class JaminanFileItem @JvmOverloads constructor(
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
        path: String,
        position: Int,
        onChangeClick: (String, Int) -> Unit,
        onDelete: (Int) -> Unit
    ) {
        layout.apply {
            showPreviewImage(path, context)
            root.setOnClickListener {
                onChangeClick(path, position)
            }
            clUploadFile.setOnClickListener {
                onChangeClick(path, position)
            }
            tvGantiData.setOnClickListener {
                onChangeClick(path, position)
            }
            btnTrash.setOnClickListener {
                onDelete(position)
            }
        }
    }

    companion object {
        fun toViewItem(
            path: String,
            position: Int,
            onChange: (String, Int) -> Unit,
            onDelete: (Int) -> Unit
        ): ViewItem<JaminanFileItem> {
            return ViewItem(
                builder = {
                    JaminanFileItem(it)
                },
                binder = {
                    it.bind(path, position, onChange, onDelete)
                },
                type = JaminanFileItem::class.hashCode()
            )
        }
    }
}