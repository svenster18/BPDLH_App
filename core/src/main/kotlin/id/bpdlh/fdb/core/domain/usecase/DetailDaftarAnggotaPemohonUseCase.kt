package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.DaftarPermohonanRepositoryContract
import id.bpdlh.fdb.core.domain.common.DetailDaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarAnggotaPemohonNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import okhttp3.internal.toImmutableList

/**
 * Created by hahn on 26/09/23.
 * Project: BPDLH App
 */
class DetailDaftarAnggotaPemohonUseCase(private val repo: DaftarPermohonanRepositoryContract): DetailDaftarAnggotaPemohonUseCaseContract {

    override fun fetchDetailDaftarAnggota(memberApplicationId: String): Single<ResultState<List<DataDebiturEntity>>> {
        return repo.fetchGroupMemberApplication(memberApplicationId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val result = mutableListOf<DataDebiturEntity>()
                response.forEach { data ->
                    val debitur = DataDebiturEntity(
                        id = data.memberApplicationDetail?.userId.orEmpty(),
                        nik = data.ktp.orEmpty(),
                        nama = data.name.orEmpty(),
                        tanggalLahir = data.dateOfBirth.orEmpty(),
                        email = data.email.orEmpty(),
                        noTelp = data.phoneNumber.orEmpty(),
                        tanggalDisetujui = "-",
                        disetujuiOleh = "-"
                    )
                    result.add(debitur)
                }
                result.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataDebiturEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchDetailDaftarAnggotaPemohonNonSosial(): ResultState<DaftarAnggotaPemohonNonSosialEntity> {
        val dataDebiturs = listOf(
            DataDebiturNonSosialEntity(
                nik = "1234567890123456",
                nama = "John Doe",
                tanggalLahir = "12 April 1998",
                email = "mail@gmail.com",
                noTelp = "029282726727",
                jenisLayanan = "Tunda terbang",
                nilaiPermohonan = 50000000
            ),
            DataDebiturNonSosialEntity(
                nik = "1234567890123456",
                nama = "John Doe",
                tanggalLahir = "12 April 1998",
                email = "mail@gmail.com",
                noTelp = "029282726727",
                jenisLayanan = "Hasil Hutan Bukan Kayu",
                nilaiPermohonan = 50000000
            ),
        )

        val daftarAnggotaPemohonEntity = DaftarAnggotaPemohonNonSosialEntity(
            tanggalPengajuanAnggota = "12 Agustus 1945",
            totalAnggotaDiajukan = 10000,
            totalNilaipermohonan = 10000000,
            tanggalDisetujui = "17 Agustus 2022",
            daftarAnggota = dataDebiturs
        )
        return ResultState.Success(daftarAnggotaPemohonEntity)
    }
}