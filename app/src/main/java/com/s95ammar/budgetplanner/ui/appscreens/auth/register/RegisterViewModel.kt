package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidator
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _displayValidationResult = EventMutableLiveData<ValidationErrors>()
    private val _onRegisterResult = EventMutableLiveData<Result>()

    val displayValidationResult = _displayValidationResult.asEventLiveData()
    val onRegisterResult = _onRegisterResult.asEventLiveData()

    fun onRegister(userRegisterInputData: UserRegisterInputData) {
        val validator = RegisterValidator(userRegisterInputData)
        _displayValidationResult.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { userCredentials -> register(userCredentials) }
            .onError { validationErrors -> _displayValidationResult.call(validationErrors) }
    }

    fun register(userCredentials: UserCredentials) = viewModelScope.launch {
        _onRegisterResult.call(Result.Loading)

        remoteRepository.register(userCredentials)
            .onSuccess { tokenResponse ->
                tokenResponse?.let {
                    localRepository.saveAuthToken(tokenResponse.token)
                    _onRegisterResult.call(Result.Success)
                }
            }
            .onError { throwable ->
                _onRegisterResult.call(Result.Error(throwable))
            }
    }
}