package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionListEntity

interface SelfAssesmentUseCaseContract {
    fun fetchQuestion(
        param: HashMap<String, String>
    ): ResultState<SelfAssesmentQuestionListEntity>
}