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
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidator
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: handle layout config changes & process death

    private val _displayValidationResult = EventMutableLiveData<ValidationErrors>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>()
    private val _onRegisterSuccessful = EventMutableLiveDataVoid()

    val displayValidationResult = _displayValidationResult.asEventLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onRegisterSuccessful = _onRegisterSuccessful.asEventLiveData()

    fun onRegister(userRegisterInputData: UserRegisterInputData) {
        val validator = RegisterValidator(userRegisterInputData)
        _displayValidationResult.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { userCredentials -> register(userCredentials) }
            .onError { validationErrors -> _displayValidationResult.call(validationErrors) }
    }

    fun register(userCredentials: UserCredentials) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)

        remoteRepository.register(userCredentials)
            .onSuccess { tokenResponse ->
                tokenResponse?.let {
                    localRepository.saveAuthToken(tokenResponse.token)
                    _displayLoadingState.call(LoadingState.Success)
                    _onRegisterSuccessful.call()
                }
            }
            .onError { throwable ->
                _displayLoadingState.call(LoadingState.Error(throwable))
            }
    }
}