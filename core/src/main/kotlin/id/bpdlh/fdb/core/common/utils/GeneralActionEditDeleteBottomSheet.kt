package id.bpdlh.fdb.core.common.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.databinding.GeneralActionEditDeleteBottomSheetBinding

class GeneralActionEditDeleteBottomSheet(
    val context: Context,
    private val layoutInflater: LayoutInflater
) {
    private val binding by lazy { GeneralActionEditDeleteBottomSheetBinding.inflate(layoutInflater) }
    fun setUp(
        titleAction: String,
        titleEdit: String,
        titleDelete: String,
        callback: (AppCompatTextView, AppCompatTextView, BottomSheetDialog) -> Unit
    ) {
        with(binding) {
            tvTitle.text = titleAction
            tvEdit.text = titleEdit
            tvDelete.text = titleDelete
            val bottomSheet = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
            bottomSheet.setContentView(binding.root)
            bottomSheet.setCancelable(true)
            bottomSheet.show()
            callback(tvEdit, tvDelete, bottomSheet)
        }
    }

    fun setUp(
        titleAction: String,
        titleEdit: String,
        titleDelete: String,
        showEdit: Boolean,
        showDelete: Boolean,
        callback: (AppCompatTextView, AppCompatTextView, BottomSheetDialog) -> Unit
    ) {
        with(binding) {
            tvTitle.text = titleAction
            tvEdit.text = titleEdit
            tvDelete.text = titleDelete
            if (showEdit) tvEdit.visible() else tvEdit.gone()
            if (showDelete) tvDelete.visible() else tvDelete.gone()
            val bottomSheet = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
            bottomSheet.setContentView(binding.root)
            bottomSheet.setCancelable(true)
            bottomSheet.show()
            callback(tvEdit, tvDelete, bottomSheet)
        }
    }
}