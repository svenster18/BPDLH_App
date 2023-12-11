package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.databinding.FragmentListKegiatanBinding
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

class KegiatanListFragment : BaseFormDaftarNonSosialFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentListKegiatanBinding
    private var position = 0
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var kegiatanList = mutableListOf<ActivityEntity>()

    private var communicator: IFormulirPendaftaranKelompokNonSosialCommunicator?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokNonSosialCommunicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListKegiatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        with(binding) {
            getData()
            setData();
            rvDataKegiatan.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahKegiatan.setOnClickListener {
                activity?.supportFragmentManager?.let { it ->
                    val tambahKegiatanBottomSheet =
                        TambahKegiatanFragment(
                            onAddKegiatan = {
                                kegiatanList.add(it)
                                when(position){
                                    0 -> listActivities.value = kegiatanList
                                    1 -> listMitraUsaha.value = kegiatanList
                                    2 -> listGambaranUmum.value = kegiatanList
                                }
                                setData();
                            },
                            type = typeTambahKegiatan(position)
                        )
                    tambahKegiatanBottomSheet.show(
                        activity?.supportFragmentManager!!,
                        GeneralConfirmationBottomSheet.TAG
                    )
                }
            }
            btnTambahKegiatan.text = getButtonText(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getData(){
        when(position){
            0 -> listActivities.value?.let { kegiatanList.addAll(it) }
            1 -> listMitraUsaha.value?.let { kegiatanList.addAll(it) }
            2 -> listGambaranUmum.value?.let { kegiatanList.addAll(it) }
        }
    }

    private fun setData() {
        val items = kegiatanList.mapIndexed { index, activityEntity ->
            KegiatanItem.
            toViewItem(
                activityEntity,
                type = typeTambahKegiatan(position),
                onEdit = {
                    activity?.supportFragmentManager?.let { it ->
                        val tambahKegiatanBottomSheet =
                            TambahKegiatanFragment(
                                activityEntity = activityEntity,
                                onAddKegiatan = {
                                    kegiatanList[index] = it
                                    when (position) {
                                        0 -> listActivities.value = kegiatanList
                                        1 -> listMitraUsaha.value = kegiatanList
                                        2 -> listGambaranUmum.value = kegiatanList
                                    }
                                    setData()
                                },
                                type = typeTambahKegiatan(position)
                            )
                        tambahKegiatanBottomSheet.show(
                            activity?.supportFragmentManager!!,
                            GeneralConfirmationBottomSheet.TAG
                        )
                    }
                },
                onDelete = {
                    kegiatanList.removeAt(index)
                    when (position) {
                        0 -> listActivities.value = kegiatanList
                        1 -> listMitraUsaha.value = kegiatanList
                        2 -> listGambaranUmum.value = kegiatanList
                    }
                    setData()
                }
            )
        }
        itemAdapter.set(items)
        with(binding) {
            if (kegiatanList.size > 0) {
                rvDataKegiatan.visible()
                ivEmpty.gone()
                tvEmptyTitle.gone()
                tvEmptyMessage.gone()
            } else {
                binding.rvDataKegiatan.gone()
                ivEmpty.visible()
                tvEmptyTitle.visible()
                tvEmptyMessage.visible()
            }
        }
    }
    private fun typeTambahKegiatan(position: Int): String {
        return when (position) {
            0 -> Constants.KEGIATAN_KELOMPOK
            1 -> Constants.MITRA_USAHA
            2 -> Constants.GAMBARAN_UMUM
            else -> ""
        }
    }

    private fun getButtonText(position: Int): String {
        return when(position) {
            0 -> getString(R.string.tambah_kegiatan_kelompok)
            1 -> getString(R.string.tambah_mitra_usaha)
            2 -> getString(R.string.tambah_gambaran_umum)
            else -> ""
        }
    }
    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {

        const val POSITION = "position"

        fun newInstance(position: Int) =
            KegiatanListFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}