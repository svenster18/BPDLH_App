package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.CountryListEntity

interface EditAkunUseCaseContract {
    fun fetchCountry(
        param: HashMap<String, String>
    ): ResultState<CountryListEntity>
}