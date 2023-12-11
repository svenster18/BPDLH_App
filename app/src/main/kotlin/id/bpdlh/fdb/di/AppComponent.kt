package id.bpdlh.fdb.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import id.bpdlh.fdb.BaseApplication
import id.bpdlh.fdb.core.di.AppScope
import id.bpdlh.fdb.core.di.DatabaseModule
import id.bpdlh.fdb.core.di.NetworkModule
import id.bpdlh.fdb.core.di.RepositoryModule
import id.bpdlh.fdb.core.di.UseCaseModule

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class, DatabaseModule::class, RepositoryModule::class, UseCaseModule::class,
        AppBuilder::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: BaseApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}