package com.s95ammar.budgetplanner.util

import android.widget.EditText
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

var TextInputLayout.inputText: String?
        get() = editText?.text?.toString()
        set(value) { editText?.setText(value) }

val EditText.inputText: String
        get() = text.toString()

fun TabLayout.doOnTabSelected(action: (tab: TabLayout.Tab) -> Unit) {
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.let { action(it) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
}