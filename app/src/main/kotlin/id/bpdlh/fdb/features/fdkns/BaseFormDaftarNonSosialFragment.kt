package id.bpdlh.fdb.features.fdkns

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.domain.entities.ActivityEntity

abstract class BaseFormDaftarNonSosialFragment: BaseDaggerFragment() {

    abstract fun initUI()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    companion object {
        var formulirKelompokNonSosialPost: FormulirKelompokNonSosialPost = FormulirKelompokNonSosialPost()
        var skFileUrl: String? = null
        var adFileUrl: String? = null
        var companionRecomendationFileUrl: String? = null
        var listActivities = MutableLiveData<List<ActivityEntity>>()
        var listMitraUsaha = MutableLiveData<List<ActivityEntity>>()
        var listGambaranUmum = MutableLiveData<List<ActivityEntity>>()
    }
}