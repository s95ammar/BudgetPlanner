package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderEventMutableLiveDataVoid
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: handle layout config changes & process death

    private val _displayValidationResult = EventMutableLiveData<ValidationErrors>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>()
    private val _onLoginSuccessful = LoaderEventMutableLiveDataVoid { checkCachedToken() }

    val displayValidationResult = _displayValidationResult.asEventLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onLoginSuccessful = _onLoginSuccessful.asEventLiveData()

    fun onLogin(userLoginInputData: UserLoginInputData) {
        val validator = LoginValidator(userLoginInputData)
        _displayValidationResult.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { login(it.email, it.password) }
            .onError { validationErrors -> _displayValidationResult.call(validationErrors) }
    }

    private fun checkCachedToken() {
        val cachedToken = localRepository.loadAuthToken()
        if (!cachedToken.isNullOrEmpty())
            _onLoginSuccessful.call()
    }

    private fun login(email: String, password: String) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)

        remoteRepository.login(email, password)
            .onSuccess { tokenResponse ->
                tokenResponse?.let {
                    localRepository.saveAuthToken(tokenResponse.token)
                    _displayLoadingState.call(LoadingState.Success)
                    _onLoginSuccessful.call()
                }
            }
            .onError { throwable ->
                _displayLoadingState.call(LoadingState.Error(throwable))
            }
    }
}