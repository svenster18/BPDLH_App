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
import id.bpdlh.fdb.databinding.FragmentGambaranUmumBinding
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokViewModel
import javax.inject.Inject

/**
 * Created by hahn on 26/10/23.
 * Project: BPDLH App
 */
class GambaranUmumFragment: BaseDaggerFragment(), TextWatcherTextChange, IDetailKegiatanKelompokCommunicator, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }
    private lateinit var binding: FragmentGambaranUmumBinding
    private var itemAdapter = ItemAdapter<DetailKegiatanKelompokItem>()
    private val listGambaranUmum = mutableListOf<ActivityEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGambaranUmumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observe(viewModel.getGambaranUmum()) {
            listGambaranUmum.clear()
            listGambaranUmum.addAll(it)
            setRecyclerViewData()
        }
    }

    private fun setRecyclerViewData() {
        val items = ArrayList<DetailKegiatanKelompokItem>()
        itemAdapter.clear()
        listGambaranUmum.forEach {
            val item = DetailKegiatanKelompokItem().withData(it)
                .withItemType(DetailKegiatanKelompokBottomSheet.GAMBARAN_UMUM)
            item.onClick = { id ->
                DetailKegiatanKelompokBottomSheet.newInstance(
                    DetailKegiatanKelompokBottomSheet.GAMBARAN_UMUM,
                    category = it.category.orEmpty(),
                    description = it.description.orEmpty(),
                    isUpdate = true,
                    id = id
                ).show(childFragmentManager, DetailKegiatanKelompokBottomSheet.TAG)
            }
            items.add(item)
        }
        itemAdapter.add(items)
    }

    private fun initUI() {
        with(binding) {
            btnAdd.setOnClickListener {
                DetailKegiatanKelompokBottomSheet.newInstance(DetailKegiatanKelompokBottomSheet.GAMBARAN_UMUM)
                    .show(childFragmentManager, DetailKegiatanKelompokBottomSheet.TAG)
            }

            val fastAdapter = FastAdapter.with(itemAdapter)
            rvActivity.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fastAdapter
            }
        }
    }

    override fun addData(category: String, description: String?) {
        val activityEntity = ActivityEntity(System.currentTimeMillis().toString(), category, description)
        viewModel.addGambaranUmum(activityEntity)
    }

    override fun updateData(id: String, category: String, description: String?) {
        val activityEntity = ActivityEntity(id, category, description)
        viewModel.updateGambaranUmum(id, activityEntity)
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}