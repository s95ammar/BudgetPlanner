package com.s95ammar.budgetplanner.util

import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

var TextInputLayout.text: String?
    get() = editText?.text?.toString()
    set(value) {
        editText?.setText(value)
    }

fun TextInputLayout.updateTextIfNotEquals(text: String?) {
    if (this.text != text)
        this.text = text
}

fun TabLayout.doOnTabSelected(action: (tab: TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { action(it) }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
}

fun TextView.setDrawableTint(@ColorInt color: Int) {
    compoundDrawables.filterNotNull().firstOrNull()?.setTint(color)
}