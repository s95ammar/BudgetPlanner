package com.s95ammar.budgetplanner.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

var TextInputLayout.inputText: String?
        get() = editText?.text?.toString()
        set(value) { editText?.setText(value) }

val EditText.inputText: String
        get() = text.toString()