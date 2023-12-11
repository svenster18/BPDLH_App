package id.bpdlh.fdb.core.network

import android.app.Application
import id.bpdlh.fdb.core.BuildConfig
import id.bpdlh.fdb.core.common.utils.RxErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkHelper {
    fun retrofitClient(application: Application, url: String = BuildConfig.BASE_URL): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(application))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient(application))
            .build()
    }

    fun retrofitIdentityClient(
        application: Application,
        url: String = BuildConfig.BASE_URL_IDENTITY,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(application))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient(application))
            .build()
    }

    fun retrofitFileServiceClient(
        application: Application,
        url: String = BuildConfig.BASE_URL_FILE_SERVICE
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(application))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient(application))
            .build()
    }

    fun retrofitMasterClient(application: Application, url: String = BuildConfig.BASE_URL_API_MASTER): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(application))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient(application))
            .build()
    }

    // TODO("Add SSL Pinning")

//    private fun certificatePinner() = CertificatePinner.Builder()
//        .add(BuildConfig.SSL_HOST, BuildConfig.SSL_PIN_1)
//        .add(BuildConfig.SSL_HOST, BuildConfig.SSL_PIN_2)
//        .add(BuildConfig.SSL_HOST, BuildConfig.SSL_PIN_3)
//        .build()

    private fun okHttpClient(application: Application): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
//            .certificatePinner(certificatePinner())
//            .addInterceptor(CustomInterceptor(application))
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideCacheInterceptor())
            .addInterceptor(FirebaseInterceptor(application))
            .callTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        return okhttp.build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    private fun provideCacheInterceptor() = run {
        okhttp3.Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }
}