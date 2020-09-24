package com.s95ammar.budgetplanner.util

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

val TextInputLayout.inputText: String
        get() = editText?.text?.toString().orEmpty()

val EditText.inputText: String
        get() = text.toString()