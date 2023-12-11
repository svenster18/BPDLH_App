package id.bpdlh.fdb.features.fdk.step3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.databinding.FragmentMitraUsahaBinding
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokViewModel
import id.bpdlh.fdb.features.fdk.step3.DetailKegiatanKelompokBottomSheet.Companion.MITRA_USAHA
import id.bpdlh.fdb.features.fdk.step3.DetailKegiatanKelompokBottomSheet.Companion.TAG
import javax.inject.Inject

/**
 * Created by hahn on 26/10/23.
 * Project: BPDLH App
 */
class MitraUsahaFragment: BaseDaggerFragment(), TextWatcherTextChange, IDetailKegiatanKelompokCommunicator, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }
    private lateinit var binding: FragmentMitraUsahaBinding
    private var itemAdapter = ItemAdapter<DetailKegiatanKelompokItem>()
    private val listMitraUsaha = mutableListOf<ActivityEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMitraUsahaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observe(viewModel.getMitraUsaha()) {
            listMitraUsaha.clear()
            listMitraUsaha.addAll(it)
            setRecyclerViewData()
        }
    }

    private fun setRecyclerViewData() {
        val items = ArrayList<DetailKegiatanKelompokItem>()
        itemAdapter.clear()
        listMitraUsaha.forEach {
            val item = DetailKegiatanKelompokItem().withData(it)
                .withItemType(MITRA_USAHA)
            item.onClick = { id ->
                DetailKegiatanKelompokBottomSheet.newInstance(
                    MITRA_USAHA,
                    category = it.category.orEmpty(),
                    isUpdate = true,
                    id = id
                ).show(childFragmentManager, TAG)
            }
            items.add(item)
        }
        itemAdapter.add(items)
    }

    private fun initUI() {
        with(binding) {
            btnAdd.setOnClickListener {
                DetailKegiatanKelompokBottomSheet.newInstance(MITRA_USAHA)
                    .show(childFragmentManager, TAG)
            }
            val fastAdapter = FastAdapter.with(itemAdapter)
            rvActivity.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fastAdapter
            }
        }
    }

    override fun addData(category: String, description: String?) {
        val activityEntity = ActivityEntity(System.currentTimeMillis().toString(), category)
        viewModel.addMitraUsaha(activityEntity)
    }

    override fun updateData(id: String, category: String, description: String?) {
        val activityEntity = ActivityEntity(id, category)
        viewModel.updateMitraUsaha(id, activityEntity)
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}