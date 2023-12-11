package id.bpdlh.fdb.core.network

import android.app.Application
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import java.nio.charset.Charset
import okhttp3.Interceptor
import okhttp3.Response

class FirebaseInterceptor(private val application: Application) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            in 400..599 -> {
                val userId = LocalPreferences(application).getValueString(Constants.USER_ID) ?: ""
                val userType =
                    LocalPreferences(application).getValueString(Constants.USER_TYPE) ?: ""
                val userWarehouseId =
                    LocalPreferences(application).getValueString(Constants.WAREHOUSE_ID) ?: ""

                val sourceBody = response.body?.source()
                sourceBody?.request(Long.MAX_VALUE)
                val sourceBuffer = sourceBody?.buffer
                val responseBodyString = sourceBuffer?.clone()?.readString(Charset.forName("UTF-8"))

                val urlSplit = response.request.url.toString().split("/")
                val feature = getLastPath(urlSplit)

                val exception = Exception(responseBodyString)

                Firebase.crashlytics.setCustomKey(Constants.USER_ID, userId)
                Firebase.crashlytics.setCustomKey(Constants.USER_TYPE, userType)
                Firebase.crashlytics.setCustomKey(Constants.WAREHOUSE_ID, userWarehouseId)
                Firebase.crashlytics.setCustomKey(Constants.FEATURE, feature)
                Firebase.crashlytics.recordException(exception)

            }
        }

        return response
    }

    private fun getLastPath(url: List<String>): String {
        var lastPath = ""
        for (i in url.size - 3 until url.size) {
            lastPath += "/" + url[i]
        }
        return lastPath
    }

}