package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.util.inputText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        button_login.setOnClickListener { onLogin() }
        text_view_login_register.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun onLogin() {
        viewModel.login(
            input_layout_login_email.inputText.trim(),
            input_layout_login_password.inputText.trim()
        )
    }
}