package id.bpdlh.fdb.core.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import id.bpdlh.fdb.core.R

class CustomProgressDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        val displayMetrics = context.resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.7).toInt()
        val height = (displayMetrics.heightPixels * 0.13).toInt()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(width, height)
        setCancelable(false)
    }
}