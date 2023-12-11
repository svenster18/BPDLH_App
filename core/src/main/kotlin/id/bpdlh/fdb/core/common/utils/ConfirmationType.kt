package id.bpdlh.fdb.core.common.utils

import androidx.annotation.StringDef
import id.bpdlh.fdb.core.common.utils.ConfirmationType.Companion.DRAFT
import id.bpdlh.fdb.core.common.utils.ConfirmationType.Companion.LOGOUT
import id.bpdlh.fdb.core.common.utils.ConfirmationType.Companion.SAVE
import id.bpdlh.fdb.core.common.utils.ConfirmationType.Companion.SERVICE

/**
 * Created by hahn on 27/09/23.
 * Project: BPDLH App
 */
@StringDef(SAVE, DRAFT, LOGOUT, SERVICE)
@Retention(AnnotationRetention.SOURCE)
annotation class ConfirmationType {
    companion object {
        const val SAVE = "Save"
        const val DRAFT = "Draft"
        const val LOGOUT = "Logout"
        const val SERVICE = "Service"
    }
}