package id.bpdlh.fdb.core.common.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.bpdlh.fdb.core.databinding.DialogConfirmationBinding

class ConfirmationDialog(context: Context) : Dialog(context) {
    private val binding = DialogConfirmationBinding.inflate(LayoutInflater.from(context))
    fun setUp(
        titleText: String,
        messageText: String,
        btnPositive: String,
        btnNegative: String,
        cancelable: Boolean,
        callback: (AppCompatButton, AppCompatButton, AlertDialog) -> Unit
    ) {
        binding.tvTitle.text = titleText
        binding.tvDesc.text = messageText
        binding.btnPositive.text = btnPositive
        binding.btnNegative.text = btnNegative
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(cancelable)
            .show()
        callback(binding.btnPositive, binding.btnNegative, dialog)
    }
}