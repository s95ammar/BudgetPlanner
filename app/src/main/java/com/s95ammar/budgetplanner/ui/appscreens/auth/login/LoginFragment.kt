package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentLoginBinding
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.util.inputText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private var binding: FragmentLoginBinding
        get() = getBinding()
        set(value) = setBinding(value)

    private val viewModel: LoginViewModel by viewModels()

    override fun initBinding(view: View) {
        binding = FragmentLoginBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.buttonLogin.setOnClickListener { onLogin() }
        binding.textViewRegister.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        navController.navigate(
            R.id.action_loginFragment_to_registerFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        )
    }

    private fun onLogin() {
        viewModel.login(
            binding.inputLayoutEmail.inputText.trim(),
            binding.inputLayoutPassword.inputText.trim()
        )
    }
}