package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentRegisterBinding
import com.s95ammar.budgetplanner.models.api.responses.errors.ConflictError
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.RegisterUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register), ViewBinder<FragmentRegisterBinding> {

    override val binding: FragmentRegisterBinding
        get() = getBinding()

    private val viewModel: RegisterViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentRegisterBinding {
        return FragmentRegisterBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.buttonRegister.setOnClickListener { viewModel.onRegister(getUserRegisterInputBundle()) }
        binding.textViewLogin.setOnClickListener { viewModel.onLogin() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun performUiEvent(uiEvent: RegisterUiEvent) {
        when (uiEvent) {
            is RegisterUiEvent.NavigateToLogin -> navigateToLogin()
            is RegisterUiEvent.DisplayValidationResult -> handleValidationErrors(uiEvent.validationErrors)
            is RegisterUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is RegisterUiEvent.NavigateToDashboard -> navigateToDashboard()
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
            RegisterValidator.ViewKeys.VIEW_EMAIL -> binding.inputLayoutEmail.error = getErrorStringById(errorId)
            RegisterValidator.ViewKeys.VIEW_PASSWORD -> binding.inputLayoutPassword.error = getErrorStringById(errorId)
            RegisterValidator.ViewKeys.VIEW_PASSWORD_CONFIRMATION -> binding.inputLayoutPasswordConfirmation.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        RegisterValidator.Errors.ERROR_EMAIL_EMPTY -> getString(R.string.error_empty_field)
        RegisterValidator.Errors.ERROR_EMAIL_INVALID -> getString(R.string.error_invalid_email)
        RegisterValidator.Errors.ERROR_EMAIL_TAKEN -> getString(R.string.error_email_taken)
        RegisterValidator.Errors.ERROR_PASSWORD_EMPTY -> getString(R.string.error_empty_field)
        RegisterValidator.Errors.ERROR_PASSWORD_LENGTH -> getString(R.string.error_registration_password_length)
        RegisterValidator.Errors.ERROR_PASSWORD_INVALID -> getString(R.string.error_invalid_password)
        RegisterValidator.Errors.ERROR_PASSWORD_CONFIRMATION_EMPTY -> getString(R.string.error_empty_field)
        RegisterValidator.Errors.ERROR_PASSWORDS_DO_NOT_MATCH -> getString(R.string.error_passwords_do_not_match)
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
            is ConflictError -> displayError(RegisterValidator.ViewKeys.VIEW_EMAIL, RegisterValidator.Errors.ERROR_EMAIL_TAKEN)
            else -> showErrorToast(throwable)
        }
    }

    private fun navigateToDashboard() {
        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToNestedNavigationDashboard())
    }

    private fun navigateToLogin() {
        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }

    private fun getUserRegisterInputBundle() = UserRegisterInputBundle(
        binding.inputLayoutEmail.inputText.orEmpty().trim(),
        binding.inputLayoutPassword.inputText.orEmpty().trim(),
        binding.inputLayoutPasswordConfirmation.inputText.orEmpty().trim(),
    )

}