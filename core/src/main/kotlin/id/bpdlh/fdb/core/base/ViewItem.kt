package id.bpdlh.fdb.core.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.IClickable
import com.mikepenz.fastadapter.items.AbstractItem

@Suppress("UNCHECKED_CAST")
class ViewItem<I : View>(
    private val builder: (context: Context) -> I,
    private val binder: (itemView: I) -> Unit,
    override val type: Int,
    private val unBinder: ((itemView: I) -> Unit)? = null,
    override var identifier: Long = -1,
) : AbstractItem<ViewItem.ViewHolder>(), IClickable<ViewItem<I>> {
    private var itemClickLister: ClickListener<ViewItem<I>>? = null
    private var preItemClickLister: ClickListener<ViewItem<I>>? = null

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        binder.invoke(holder.itemView as I)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        unBinder?.invoke(holder.itemView as I)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun createView(ctx: Context, parent: ViewGroup?): View {
        return builder(ctx)
    }

    override val layoutRes: Int = 0 // useless

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override var onItemClickListener: ClickListener<ViewItem<I>>?
        get() = itemClickLister
        set(value) {
            itemClickLister = value
        }

    override var onPreItemClickListener: ClickListener<ViewItem<I>>?
        get() = preItemClickLister
        set(value) {
            preItemClickLister = value
        }

    fun withOnItemClickListener(listener: ClickListener<ViewItem<I>>): ViewItem<I> {
        itemClickLister = listener
        return this
    }

    fun withOnPreItemClickListener(listener: ClickListener<ViewItem<I>>): ViewItem<I> {
        preItemClickLister = listener
        return this
    }

    fun withIdentifier(id: Long): ViewItem<I> {
        identifier = id
        return this
    }

    fun withIdentifier(id: String): ViewItem<I> {
        identifier = id.hashCode().toLong()
        return this
    }

}
