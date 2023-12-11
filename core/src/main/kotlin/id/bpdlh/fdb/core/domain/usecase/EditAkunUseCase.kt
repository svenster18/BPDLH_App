package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.EditAkunUseCaseContract
import id.bpdlh.fdb.core.domain.entities.CountryListEntity
import java.util.Locale


class EditAkunUseCase : EditAkunUseCaseContract {
    override fun fetchCountry(param: HashMap<String, String>): ResultState<CountryListEntity> {
        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()

        val countryData = CountryListEntity(
            data = countries,
            maxPage = 1,
            maxData = countries.size
        )

        return ResultState.Success(data = countryData)
    }

}