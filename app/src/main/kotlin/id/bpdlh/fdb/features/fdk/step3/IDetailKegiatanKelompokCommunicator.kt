package id.bpdlh.fdb.features.fdk.step3

/**
 * Created by hahn on 26/10/23.
 * Project: BPDLH App
 */
interface IDetailKegiatanKelompokCommunicator {
    fun addData(category: String, description: String? = null)

    fun updateData(id: String, category: String, description: String? = null)
}