package id.bpdlh.fdb.core.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CustomTextWatcher {

    var callback: TextWatcherAfterChange? = null
    var callbackTextChange: TextWatcherTextChange? = null

    fun setCallBackAfterChange(callback: TextWatcherAfterChange): CustomTextWatcher {
        this.callback = callback
        return this
    }

    fun setCallBackOnTextChange(callback: TextWatcherTextChange): CustomTextWatcher {
        this.callbackTextChange = callback
        return this
    }

    fun registerEditText(editText: EditText): CustomTextWatcher {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                callback?.onAfterTextChanged(editText, p0)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                callbackTextChange?.onTextChanged(editText, p0, p1, p2, p3)
            }

        })
        return this

    }
}