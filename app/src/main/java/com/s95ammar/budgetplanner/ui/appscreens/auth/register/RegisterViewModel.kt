package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.RegisterUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidator
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: handle layout config changes & process death

    private val _performUiEvent = EventMutableLiveData<RegisterUiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onRegister(userRegisterInputBundle: UserRegisterInputBundle) {
        val validator = RegisterValidator(userRegisterInputBundle)
        _performUiEvent.call(RegisterUiEvent.DisplayValidationResult(validator.getValidationErrors(allBlank = true)))

        validator.getValidationResult()
            .onSuccess { userCredentials -> register(userCredentials) }
            .onError { validationErrors -> _performUiEvent.call(RegisterUiEvent.DisplayValidationResult(validationErrors)) }
    }

    fun onLogin() {
        _performUiEvent.call(RegisterUiEvent.NavigateToLogin)
    }

    private fun register(userCredentials: UserCredentials) = viewModelScope.launch {
        _performUiEvent.call(RegisterUiEvent.DisplayLoadingState(LoadingState.Loading))

        remoteRepository.register(userCredentials)
            .onSuccess { tokenResponse ->
                tokenResponse?.let {
                    localRepository.saveAuthToken(tokenResponse.token)
                    _performUiEvent.call(RegisterUiEvent.DisplayLoadingState(LoadingState.Success))
                    _performUiEvent.call(RegisterUiEvent.NavigateToDashboard)
                }
            }
            .onError { throwable ->
                _performUiEvent.call(RegisterUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
    }
}