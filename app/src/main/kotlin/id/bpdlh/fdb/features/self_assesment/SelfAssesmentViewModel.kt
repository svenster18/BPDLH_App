package id.bpdlh.fdb.features.self_assesment

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.SelfAssesmentUseCaseContract
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionListEntity
import javax.inject.Inject

class SelfAssesmentViewModel @Inject constructor(
    private val selfAssesmentUseCase: SelfAssesmentUseCaseContract
) :
    BaseViewModel() {

    val result = MutableLiveData<ResultState<SelfAssesmentQuestionListEntity>>()

    fun fetchQuestion(page: Int) {
        val parameters = HashMap<String, String>()
        parameters["size"] = "0"
        parameters["page"] = page.toString()

        result.value = selfAssesmentUseCase.fetchQuestion(parameters)
    }
}