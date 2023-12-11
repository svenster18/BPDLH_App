package id.bpdlh.fdb.core.common.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.bpdlh.fdb.core.databinding.GeneralInfoBottomSheetBinding

class GeneralInfoBottomSheet(val context: Context, val layoutInflater: LayoutInflater) {
    private val binding by lazy { GeneralInfoBottomSheetBinding.inflate(layoutInflater) }
    fun setUp(
        imageInfo: Int, mainInformation: String, description: String,
        callback: (Button, BottomSheetDialog) -> Unit
    ) {
        with(binding) {
            image.load(imageInfo)
            mainInfo.text = mainInformation
            secondaryInfo.text = description
            val bottomSheet = BottomSheetDialog(context)
            bottomSheet.setContentView(binding.root)
            bottomSheet.setCancelable(false)
            bottomSheet.show()
            callback(actionButton, bottomSheet)
        }
    }

    fun setUp(
        imageInfo: Int, mainInformation: String, description: String, btnText: String,
        callback: (Button, BottomSheetDialog) -> Unit
    ) {
        with(binding) {
            image.load(imageInfo)
            mainInfo.text = mainInformation
            secondaryInfo.text = description
            actionButton.text = btnText.uppercase()
            val bottomSheet = BottomSheetDialog(context)
            bottomSheet.setContentView(binding.root)
            bottomSheet.setCancelable(false)
            bottomSheet.show()
            callback(actionButton, bottomSheet)
        }
    }
}