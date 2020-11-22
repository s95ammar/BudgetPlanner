package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.util.inputText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment: BaseFragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        button_register.setOnClickListener { onRegister() }

    }

    private fun onRegister() {
        viewModel.register(
            input_layout_register_email.inputText.trim(),
            input_layout_register_password.inputText.trim()
        )
    }

}