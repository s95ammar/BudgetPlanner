package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentRegisterBinding
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.util.inputText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private var binding: FragmentRegisterBinding
        get() = getBinding()
        set(value) = setBinding(value)

    private val viewModel: RegisterViewModel by viewModels()

    override fun initBinding(view: View) {
        binding = FragmentRegisterBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.buttonRegister.setOnClickListener { onRegister() }
        binding.textViewLogin.setOnClickListener { navigateToLogin() }
    }

    private fun navigateToLogin() {
        navController.navigate(
            R.id.action_registerFragment_to_loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build()
        )
    }

    private fun onRegister() {
        viewModel.register(
            binding.inputLayoutEmail.inputText.trim(),
            binding.inputLayoutPassword.inputText.trim()
        )
    }

}