package com.s95ammar.budgetplanner.ui.appscreens.auth.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: clean

    fun login(email: String, password: String) = viewModelScope.launch {
        remoteRepository.login(email, password).body()?.let { tokenResponse ->
            localRepository.saveAuthToken(tokenResponse.token)
        }
    }
}