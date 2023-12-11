package id.bpdlh.fdb.core.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.databinding.ItemLoadMoreBinding

class LoadMoreItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val layout = ItemLoadMoreBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun bind(item: Boolean?) {
        item?.let {}
    }

    companion object {
        fun toViewItem(
            data: Boolean?
        ): ViewItem<LoadMoreItem> {
            return ViewItem(
                builder = {
                    LoadMoreItem(it)
                },
                binder = {
                    it.bind(data)
                },
                type = LoadMoreItem::class.hashCode()
            )
        }
    }
}
