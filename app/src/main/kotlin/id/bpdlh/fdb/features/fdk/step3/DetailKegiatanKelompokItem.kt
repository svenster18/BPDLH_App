package id.bpdlh.fdb.features.fdk.step3

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.databinding.ItemKegiatanKelompokBinding
import id.bpdlh.fdb.features.fdk.step3.DetailKegiatanKelompokBottomSheet.Companion.KEGIATAN_KELOMPOK
import id.bpdlh.fdb.features.fdk.step3.DetailKegiatanKelompokBottomSheet.Companion.MITRA_USAHA

/**
 * Created by hahn on 26/10/23.
 * Project: BPDLH App
 */
class DetailKegiatanKelompokItem: AbstractBindingItem<ItemKegiatanKelompokBinding>() {
    private var data: ActivityEntity? = null
    var onClick: ((String) -> Unit)? = null
    private var itemType: String = "KEGIATAN_KELOMPOK"

    override val type: Int
        get() = R.id.cl

    fun withData(data: ActivityEntity): DetailKegiatanKelompokItem {
        this.data = data
        return this
    }

    fun withItemType(itemType: String): DetailKegiatanKelompokItem {
        this.itemType = itemType
        return this
    }

    override fun bindView(binding: ItemKegiatanKelompokBinding, payloads: List<Any>) {
        setItemType(itemType, binding)
        with(binding) {
            tvKategoriValue.text = data?.category.orEmpty()
            tvDeskripsiValue.text = data?.description.orEmpty()
            btnEdit.setOnClickListener {
                onClick?.invoke(data?.id.orEmpty())
            }
        }
    }

    private fun setItemType(itemType: String, binding: ItemKegiatanKelompokBinding) {
        with(binding) {
            when(itemType) {
                KEGIATAN_KELOMPOK -> {
                    tvKategori.text = tvKategori.context.getString(R.string.text_kategori)
                    tvDeskripsi.visible()
                    tvDeskripsiValue.visible()
                }
                MITRA_USAHA -> {
                    tvKategori.text = tvKategori.context.getString(R.string.text_kategori_usaha)
                    tvDeskripsi.gone()
                    tvDeskripsiValue.gone()
                }
                else -> {
                    tvKategori.text = tvKategori.context.getString(R.string.text_kategori_usaha)
                    tvDeskripsi.visible()
                    tvDeskripsiValue.visible()
                }
            }
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemKegiatanKelompokBinding {
        return ItemKegiatanKelompokBinding.inflate(inflater, parent, false)
    }
}