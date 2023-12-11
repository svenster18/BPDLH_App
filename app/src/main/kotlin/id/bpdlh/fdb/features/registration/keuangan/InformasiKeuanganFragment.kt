package id.bpdlh.fdb.features.registration.keuangan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.domain.entities.UsahaLainEntity
import id.bpdlh.fdb.databinding.FragmentInformasiKeuanganBinding
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class InformasiKeuanganFragment : BaseRegistrationFragment(), TextWatcherTextChange, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private lateinit var binding: FragmentInformasiKeuanganBinding
    private var requestCode = 0
    private var dokumenPendukungFile: File? = null
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
//    private val usahaLainList = arrayListOf<UsahaLainEntity>()
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null && requestCode == Constants.REQUEST_CODE_DOKUMEN_PENDUKUNG) {
                    dokumenPendukungFile = uriToFile(uri, requireContext())
                    if (dokumenPendukungFile != null) registrasiPerorangan.dokumenPendukungPath =
                        (dokumenPendukungFile as File).path
                    binding.cvDokumenPendukung.showPreviewImage(uri, requireContext())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInformasiKeuanganBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            val pengeluaranRutinError =
                textILPengeluaranRutin.editText?.text.toString().getError(requireContext())
            textILPengeluaranRutin.error(pengeluaranRutinError)
            if (pengeluaranRutinError != null) return false
            val pengeluaranTerbesarError =
                textILPengeluaranTerbesar.editText?.text.toString().getError(requireContext())
            textILPengeluaranTerbesar.error(pengeluaranTerbesarError)
            if (pengeluaranTerbesarError != null) return false
            val penggunaanTerbesarError =
                textILPenggunaanPengeluaranTerbesar.editText?.text.toString()
                    .getGeneralError(requireContext())
            textILPenggunaanPengeluaranTerbesar.error(penggunaanTerbesarError)
            if (penggunaanTerbesarError != null) return false
//            if (cvDokumenPendukung.ivFile.isGone) {
//                requireContext().showToast(
//                    getString(R.string.wajib_upload_dokumen_pendukung),
//                    Constants.WARNING
//                )
//                return false
//            }
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            if (registrasiPerorangan.pengeluaranRutin != 0L) edtPengeluaranRutin.setText(
                registrasiPerorangan.pengeluaranRutin.toString()
            )
            if (registrasiPerorangan.pengeluaranTerbesar != 0L) edtPengeluaranTerbesar.setText(
                registrasiPerorangan.pengeluaranTerbesar.toString()
            )
            edtPenggunaanPengeluaranTerbesar.setText(registrasiPerorangan.penggunaanTerbesar)
            cvDokumenPendukung.showPreviewImage(
                registrasiPerorangan.dokumenPendukungPath,
                requireContext()
            )
        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvUsahaLain.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahUsahaLain.setOnClickListener {
                val tambahUsahaBottomSheet = TambahUsahaBottomSheet({ usahaLain ->
                    otherBusinessList.add(usahaLain)
                    showUsahaLainList()
                })
                tambahUsahaBottomSheet.show(childFragmentManager, TambahUsahaBottomSheet.TAG)
            }
            textILPengeluaranRutin.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganFragment)
            }
            edtPengeluaranRutin.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanPendapatan = edtPengeluaranRutin.text.toString()
                if (strPerkiraanPendapatan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanPendapatan.extractNumber())
                        edtPengeluaranRutin.setText(strPerkiraanPendapatan.extractNumber())
                    } else edtPengeluaranRutin.setText(currencyFormatter(strPerkiraanPendapatan.toLong(), false))
                }
            }
            textILPengeluaranTerbesar.editText?.let {
                CustomTextWatcher().registerEditText(it).setCallBackOnTextChange(this@InformasiKeuanganFragment)
            }
            edtPengeluaranTerbesar.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanPendapatan = edtPengeluaranTerbesar.text.toString()
                if (strPerkiraanPendapatan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanPendapatan.extractNumber())
                        edtPengeluaranTerbesar.setText(strPerkiraanPendapatan.extractNumber())
                    } else edtPengeluaranTerbesar.setText(currencyFormatter(strPerkiraanPendapatan.toLong(), false))
                }
            }
            textILPenggunaanPengeluaranTerbesar.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganFragment)
            }
            cvDokumenPendukung.root.setOnClickListener {
                requestCode = Constants.REQUEST_CODE_DOKUMEN_PENDUKUNG
                startFileChooser(launcherIntentFileChooser)
            }
        }
    }

    private fun showUsahaLainList() {
        var position = 0
        runBlocking {
            val items = otherBusinessList.map { usahaLain ->
                UsahaLainItem.toViewItem(
                    usahaLain, position++
                ) { _, position ->
                    onChangeClick(usahaLain, position)
                }
            }
            launch {
                itemAdapter.set(items)
            }
        }
    }

    private fun onChangeClick(usahaLain: UsahaLainEntity, position: Int) {
        val tambahUsahaBottomSheet = TambahUsahaBottomSheet({ updatedUsahaLain ->
            otherBusinessList[position] = updatedUsahaLain
            showUsahaLainList()
        }, usahaLain)
        tambahUsahaBottomSheet.show(parentFragmentManager, TambahUsahaBottomSheet.TAG)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILPengeluaranRutin.editText -> {
                val strPengeluaranRutin = editText?.text.toString()
                if (strPengeluaranRutin.isNotEmpty()) registrasiPerorangan.pengeluaranRutin =
                    strPengeluaranRutin.extractNumber().toLong()
                binding.textILPengeluaranRutin.error =
                    strPengeluaranRutin.getError(requireContext())
            }

            binding.textILPengeluaranTerbesar.editText -> {
                val strPengeluaranTerbesar = editText?.text.toString()
                if (strPengeluaranTerbesar.isNotEmpty()) registrasiPerorangan.pengeluaranTerbesar =
                    strPengeluaranTerbesar.extractNumber().toLong()
                binding.textILPengeluaranTerbesar.error = strPengeluaranTerbesar.getError(requireContext())
            }
            binding.textILPenggunaanPengeluaranTerbesar.editText -> {
                val strPenggunaanPengeluaranTerbesar = editText?.text.toString()
                registrasiPerorangan.penggunaanTerbesar = strPenggunaanPengeluaranTerbesar
                binding.textILPenggunaanPengeluaranTerbesar.error =
                    strPenggunaanPengeluaranTerbesar.getError(requireContext())
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}