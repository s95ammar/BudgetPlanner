package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _displayValidationResult = EventMutableLiveData<ValidationErrors>()
    private val _onLoginResult = EventMutableLiveData<Result>()

    val displayValidationResult = _displayValidationResult.asEventLiveData()
    val onLoginResult = _onLoginResult.asEventLiveData()

    fun onLogin(userLoginInputData: UserLoginInputData) {
        val validator = LoginValidator(userLoginInputData)
        _displayValidationResult.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { login(it.email, it.password) }
            .onError { validationErrors -> _displayValidationResult.call(validationErrors) }
    }

    private fun login(email: String, password: String) = viewModelScope.launch {
        _onLoginResult.call(Result.Loading)

        remoteRepository.login(email, password)
            .onSuccess { tokenResponse ->
                tokenResponse?.let {
                    localRepository.saveAuthToken(tokenResponse.token)
                    _onLoginResult.call(Result.Success)
                }
            }
            .onError { throwable ->
                _onLoginResult.call(Result.Error(throwable))
            }
    }
}