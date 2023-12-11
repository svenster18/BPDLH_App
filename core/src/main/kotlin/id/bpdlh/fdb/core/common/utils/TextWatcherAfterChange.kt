package id.bpdlh.fdb.core.common.utils

import android.text.Editable
import android.widget.EditText

interface TextWatcherAfterChange {
    fun onAfterTextChanged(editText: EditText?, editable: Editable?)
}