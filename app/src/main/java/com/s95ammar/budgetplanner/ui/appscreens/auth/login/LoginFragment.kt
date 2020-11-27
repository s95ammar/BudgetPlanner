package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentLoginBinding
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.api.responses.IncorrectPasswordError
import com.s95ammar.budgetplanner.models.api.responses.UserDoesNotExistError
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.UsesViewBinding
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), UsesViewBinding<FragmentLoginBinding> {

    override val binding: FragmentLoginBinding
        get() = getBinding()

    private val viewModel: LoginViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentLoginBinding {
        return FragmentLoginBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.buttonLogin.setOnClickListener { onLogin() }
        binding.textViewRegister.setOnClickListener { navigateToRegister() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.displayValidationResult.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
        viewModel.onLoginResult.observeEvent(viewLifecycleOwner) { onLoginResult(it) }
    }
    
    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.highestPriorityOrNone)
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            LoginValidator.ViewKeys.VIEW_EMAIL -> binding.inputLayoutEmail.error = getErrorStringById(errorId)
            LoginValidator.ViewKeys.VIEW_PASSWORD -> binding.inputLayoutPassword.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        LoginValidator.Errors.ERROR_EMAIL_EMPTY -> getString(R.string.error_empty_field)
        LoginValidator.Errors.ERROR_EMAIL_INVALID -> getString(R.string.error_invalid_email)
        LoginValidator.Errors.ERROR_EMAIL_NOT_REGISTERED -> getString(R.string.error_email_not_registered)
        LoginValidator.Errors.ERROR_PASSWORD_EMPTY -> getString(R.string.error_empty_field)
        LoginValidator.Errors.ERROR_PASSWORD_LENGTH -> getString(R.string.error_login_password_length)
        LoginValidator.Errors.ERROR_PASSWORD_INVALID -> getString(R.string.error_invalid_password)
        LoginValidator.Errors.ERROR_PASSWORD_INCORRECT -> getString(R.string.error_incorrect_password)
        else -> null
    }

    private fun onLoginResult(result: Result) {
        when (result) {
            is Result.Loading -> loadingManager?.showLoading()
            is Result.Error -> {
                loadingManager?.hideLoading()
                handleLoginError(result.throwable)
            }
            is Result.Success -> {
                loadingManager?.hideLoading()
                navigateToCurrentBudget()
            }
        }
    }

    private fun handleLoginError(throwable: Throwable) {
        when (throwable) {
            is IncorrectPasswordError -> displayError(LoginValidator.ViewKeys.VIEW_PASSWORD, LoginValidator.Errors.ERROR_PASSWORD_INCORRECT)
            is UserDoesNotExistError -> displayError(LoginValidator.ViewKeys.VIEW_EMAIL, LoginValidator.Errors.ERROR_EMAIL_NOT_REGISTERED)
            else -> throwable.message?.let { showToast(it) }
        }
    }

    private fun navigateToCurrentBudget() {
        navController.navigate(
            R.id.action_loginFragment_to_navigation_current_budget,
            null,
            NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build()
        )
    }
    
    private fun navigateToRegister() {
        navController.navigate(
            R.id.action_loginFragment_to_registerFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        )
    }

    private fun onLogin() {
        viewModel.onLogin(
            UserLoginInputData(
                binding.inputLayoutEmail.inputText.trim(),
                binding.inputLayoutPassword.inputText.trim()
            )
        )
    }
}