package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.RegistrasiKelompokRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.RegistrasiKelompokSourceApi
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.data.sources.remote.RegistrasiApi
import io.reactivex.Single
import okhttp3.MultipartBody

/**
 * Created by hahn on 08/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokRepository(private val api: RegistrasiApi): RegistrasiKelompokRepositoryContract {

    override fun submitData(body: RegistrasiKelompokPost): Single<BaseDataSourceApi<RegistrasiKelompokSourceApi>> {

        //TODO: ada cara convert dari data class ke Multipart?
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", body.email ?: "")
            .addFormDataPart("password", body.password ?: "")
            .addFormDataPart("password_confirmation", body.passwordConfirmation ?: "")
            .addFormDataPart("name", body.name ?: "")
            .addFormDataPart("type", body.type ?: "")
            .addFormDataPart("sk", body.sk ?: "")
            .addFormDataPart("province", body.province ?: "")
            .addFormDataPart("city", body.city ?: "")
            .addFormDataPart("district", body.district ?: "")
            .addFormDataPart("village", body.village ?: "")
            .addFormDataPart("address", body.address ?: "")
            .addFormDataPart("contact_person_name", body.contactPersonName ?: "")
            .addFormDataPart("contact_person_position", body.contactPersonPosition ?: "")
            .addFormDataPart("contact_person_ktp", body.contactPersonKtp ?: "")
            .addFormDataPart("contact_person_phone_number", body.contactPersonPhoneNumber ?: "")
            .addFormDataPart("contact_person_email", body.contactPersonEmail ?: "")
            .build()

        return api.submitData(multipartBody)
    }
}