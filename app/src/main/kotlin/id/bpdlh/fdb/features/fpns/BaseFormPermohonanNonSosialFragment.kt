package id.bpdlh.fdb.features.fpns

import android.os.Bundle
import android.view.View
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.data.post.MemberApplicationPost

abstract class BaseFormPermohonanNonSosialFragment: BaseDaggerFragment() {

    abstract fun initUI()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    companion object {
        var memberApplicationId: String = ""
        var memberApplicationPost: MemberApplicationPost = MemberApplicationPost()
        var ijinLahanFileUrl: String? = null
        var suratTanahFileUrl: String? = null
        var suratJualBeliFileUrl: String? = null
        var spptFileFileUrl: String? = null
        var fotoLahanFileUrl: String? = null
        var dokumenLainnyaFileUrl: String? = null
        var labaRugiFileUrl: String? = null
    }
}