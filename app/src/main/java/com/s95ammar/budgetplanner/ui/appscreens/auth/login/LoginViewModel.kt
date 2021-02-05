package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.AuthRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.LoginUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: AuthRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<LoginUiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    init {
        checkCachedToken()
    }

    fun onLogin(userLoginInputBundle: UserLoginInputBundle) {
        val validator = LoginValidator(userLoginInputBundle)
        _performUiEvent.call(LoginUiEvent.DisplayValidationResult(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { login(it.email, it.password) }
            .onError { validationErrors -> _performUiEvent.call(LoginUiEvent.DisplayValidationResult(validationErrors)) }
    }

    fun onRegister() {
        _performUiEvent.call(LoginUiEvent.NavigateToRegister)
    }

    private fun checkCachedToken() {
        if (repository.tokenExists())
            _performUiEvent.call(LoginUiEvent.NavigateToDashboard)
    }

    private fun login(email: String, password: String) = viewModelScope.launch {
        _performUiEvent.call(LoginUiEvent.DisplayLoadingState(LoadingState.Loading))

        repository.login(email, password)
            .catch { throwable ->
                _performUiEvent.call(LoginUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect {
                _performUiEvent.call(LoginUiEvent.DisplayLoadingState(LoadingState.Success))
                _performUiEvent.call(LoginUiEvent.NavigateToDashboard)
            }
    }
}