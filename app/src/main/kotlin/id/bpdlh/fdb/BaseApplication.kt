package id.bpdlh.fdb

import android.app.Application
import id.bpdlh.fdb.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import timber.log.Timber

class BaseApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
        if (BuildConfig.DEBUG && BuildConfig.BUILD_TYPE == "debug") {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}