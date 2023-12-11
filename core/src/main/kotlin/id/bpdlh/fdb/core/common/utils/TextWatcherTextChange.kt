package id.bpdlh.fdb.core.common.utils

import android.widget.EditText

interface TextWatcherTextChange {
    fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int)
}