package id.bpdlh.fdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import id.bpdlh.fdb.core.CoreDeepLinkModule
import id.bpdlh.fdb.core.CoreDeepLinkModuleRegistry

@DeepLinkHandler(
    AppDeepLinkModule::class,
    CoreDeepLinkModule::class
)
class DeepLinkRouterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkDelegate = DeepLinkDelegate(
            AppDeepLinkModuleRegistry(),
            CoreDeepLinkModuleRegistry()
        )
        deepLinkDelegate.dispatchFrom(this)
        finish()
    }
}