package id.bpdlh.fdb.features.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.domain.entities.HomeSectionEntity
import id.bpdlh.fdb.databinding.LayoutSectionContentBinding

/**
 * Created by hahn on 27/09/23.
 * Project: BPDLH App
 */
class HomeSectionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding = LayoutSectionContentBinding.inflate(
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
        data: HomeSectionEntity,
        onClick: (Int) -> Unit
    ) {
        with(binding) {
            tvSectionMenu.text = data.title
            data.icon?.let { ivSectionMenu.setImageResource(it) }
            root.setOnClickListener {
                data.icon?.let { icon -> onClick(icon) }
            }
        }
    }

    companion object {
        fun toViewItem(data: HomeSectionEntity, onClick: (Int) -> Unit): ViewItem<HomeSectionItem> {
            return ViewItem(
                builder = { HomeSectionItem(it) },
                binder = { it.bind(data, onClick)},
                type = HomeSectionItem::class.hashCode()
            )
        }
    }
}
