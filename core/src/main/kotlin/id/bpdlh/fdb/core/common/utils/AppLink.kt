package id.bpdlh.fdb.core.common.utils

import id.bpdlh.fdb.core.BuildConfig.NAV_SCHEME


object AppLink {
    const val SCHEME = NAV_SCHEME

    object Auth {
        const val LOGIN = "$SCHEME://login/{expired}"
        const val FORGET_PASSWORD = "$SCHEME://forget_password"
        const val OTP_VERIFICATION = "$SCHEME://otp_verification"
        const val PARAM_EXP = "expired"
        const val PARAM_TOKEN_SALES = "accessToken"
    }
}