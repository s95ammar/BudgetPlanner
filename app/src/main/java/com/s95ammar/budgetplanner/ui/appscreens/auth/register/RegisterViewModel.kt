package com.s95ammar.budgetplanner.ui.appscreens.auth.register

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.AuthUtil
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputData
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidationErrors
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidationViewKeys
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation.RegisterValidator
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: clean

    fun onRegister(userRegisterInputData: UserRegisterInputData) {
        val validator = RegisterValidator(userRegisterInputData)

        validator.getValidationResult()
            .onSuccess { userCredentials -> register(userCredentials) }
            .onError { validationErrors ->  /*TODO*/ }

    }

    fun register(userCredentials: UserCredentials) = viewModelScope.launch {
        remoteRepository.register(userCredentials).body()?.let { tokenResponse ->
            localRepository.saveAuthToken(tokenResponse.token)
        }
    }
}