package id.bpdlh.fdb.features.permohonan_pembiayaan

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.PembiayaanPerhutananUseCaseContract
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import timber.log.Timber
import javax.inject.Inject

class PembiayaanPerhutananViewModel @Inject constructor(
    private val pembiayaanPerhutananUseCase: PembiayaanPerhutananUseCaseContract
) : BaseViewModel() {

    val result = MutableLiveData<ResultState<List<PembiayaanPerhutananEntity>>>()
    fun fetchPembiayaanPermohonanPerhutananSosial(userId: String, page: Int, type: Int) {
        val parameters = HashMap<String, String>()
        parameters["size"] = "0"
        parameters["page"] = page.toString()
        addDisposable(
            pembiayaanPerhutananUseCase.fetchDataPembiayaanPerhutanan(userId, parameters, type)
                .doOnSubscribe {
                    result.value = ResultState.Loading()
                }
                .subscribe { resultState ->
                    result.value = ResultState.HideLoading()
                    result.value = resultState
                }
        )
    }

    fun fetchPembiayaanPermohonanNonPerhutananSosial(page: Int, type: Int) {
        val parameters = HashMap<String, String>()
        parameters["size"] = "0"
        parameters["page"] = page.toString()
        result.value =
            pembiayaanPerhutananUseCase.fetchDataPembiayaanNonPerhutanan(parameters, type)
        Timber.d(result.value.toString())
    }
}