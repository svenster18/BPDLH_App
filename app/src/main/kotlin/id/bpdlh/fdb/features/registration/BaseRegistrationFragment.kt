package id.bpdlh.fdb.features.registration

import android.os.Build
import android.os.Bundle
import android.view.View
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.core.domain.entities.UsahaLainEntity
import timber.log.Timber

abstract class BaseRegistrationFragment: BaseDaggerFragment() {

    var category: String? = null
    var pembiayaanPerhutananEntity: PembiayaanPerhutananEntity? = null

    abstract fun isValid(): Boolean
    abstract fun initUI()
    abstract fun disableEditText()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val catFromBundle = savedInstanceState.getString(Constants.EXTRA_CATEGORY)
            category = catFromBundle
            val ppeFromBundle = if (Build.VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable(
                    Constants.EXTRA_PEMBIAYAAN_PERHUTANAN,
                    PembiayaanPerhutananEntity::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable(Constants.EXTRA_PEMBIAYAAN_PERHUTANAN)
            }

            pembiayaanPerhutananEntity = ppeFromBundle
        }
        pembiayaanPerhutananEntity?.let {
            if (it.status != FdbBadge.DRAFT) {
                disableEditText()
            }
        }

        initUI()
        Timber.d(status.toString())
        if (status != FdbBadge.DRAFT) disableEditText()
    }

    companion object {
        val otherBusinessList = arrayListOf<UsahaLainEntity>()
        val dokumenLegalitasList = arrayListOf<FileEntity>()
        var registrasiPerorangan: RegistrasiPerorangan = RegistrasiPerorangan(
            otherBusiness = otherBusinessList
        )
        var memberApplicationPost: MemberApplicationPost = MemberApplicationPost()
        var type: String = Constants.PERHUTANAN_SOSIAL
        var serviceType: String? = null
        var status: Int = 0
    }
}