package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.ApiResponseHandler.mapToApiResult
import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) : RemoteRepository {

    override suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse> = withContext(Dispatchers.IO) {
        apiService.register(userCredentials).mapToApiResult()
    }
    override suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        apiService.login(email, password).mapToApiResult()
    }
    override suspend fun authenticate() = withContext(Dispatchers.IO) {
        apiService.authenticate().mapToApiResult()
    }

}