package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentLoginBinding
import com.s95ammar.budgetplanner.models.api.responses.errors.ForbiddenError
import com.s95ammar.budgetplanner.models.api.responses.errors.NotFoundError
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.LoginUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), ViewBinder<FragmentLoginBinding> {

    override val binding: FragmentLoginBinding
        get() = getBinding()

    private val viewModel: LoginViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentLoginBinding {
        return FragmentLoginBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.buttonLogin.setOnClickListener { viewModel.onLogin(getUserLoginInputBundle()) }
        binding.textViewRegister.setOnClickListener { viewModel.onRegister() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun performUiEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.NavigateToRegister -> navigateToRegister()
            is LoginUiEvent.DisplayValidationResult -> handleValidationErrors(uiEvent.validationErrors)
            is LoginUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is LoginUiEvent.NavigateToDashboard -> navigateToDashboard()
        }
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

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                handleError(loadingState.throwable)
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is ForbiddenError -> displayError(LoginValidator.ViewKeys.VIEW_PASSWORD, LoginValidator.Errors.ERROR_PASSWORD_INCORRECT)
            is NotFoundError -> displayError(LoginValidator.ViewKeys.VIEW_EMAIL, LoginValidator.Errors.ERROR_EMAIL_NOT_REGISTERED)
            else -> showErrorToast(throwable)
        }
    }

    private fun navigateToDashboard() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToNestedNavigationDashboard())
    }
    
    private fun navigateToRegister() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    private fun getUserLoginInputBundle() = UserLoginInputBundle(
        binding.inputLayoutEmail.inputText.orEmpty().trim(),
        binding.inputLayoutPassword.inputText.orEmpty().trim()
    )
}