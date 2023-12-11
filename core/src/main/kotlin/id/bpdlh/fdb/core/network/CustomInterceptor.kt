package id.bpdlh.fdb.core.network

import android.app.Application
import id.bpdlh.fdb.core.common.utils.AES
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor(private val application: Application) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = LocalPreferences(application).getValueString(Constants.BEARER_TOKEN) ?: ""
        val isLogin = LocalPreferences(application).getValueBoolean(Constants.LOGGED_IN, false)
        val newRequest = chain.request().newBuilder()

        return if (isLogin && accessToken.isNotEmpty()) {
            newRequest.addHeader("Authorization", "Bearer $accessToken")
            chain.proceed(newRequest.build())
        } else {
            newRequest.addHeader(
                "Authorization",
                Credentials.basic(
                    AES.decryptAES256(BuildConfig.USERNAME),
                    AES.decryptAES256(BuildConfig.PASSWORD)
                )
            )

            chain.proceed(newRequest.build())

        }
    }

}