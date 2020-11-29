package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.ApiResponseMapper.mapToApiResult
import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.common.CategoryApiEntity
import com.s95ammar.budgetplanner.models.api.requests.IdBodyRequest
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) : RemoteRepository {

    override suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse> = withContext(Dispatchers.IO) {
        tryPerform { apiService.register(userCredentials).mapToApiResult() }
    }

    override suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        tryPerform { apiService.login(email, password).mapToApiResult() }
    }

    override suspend fun authenticate() = withContext(Dispatchers.IO) {
        tryPerform { apiService.authenticate().mapToApiResult() }
    }

    override suspend fun getAllUserCategories() = withContext(Dispatchers.IO) {
        tryPerform { apiService.getCategory(null).mapToApiResult() }
    }

    override suspend fun getCategory(id: Int) = withContext(Dispatchers.IO) {
        tryPerform { apiService.getCategory(id).mapToApiResult() }
    }

    override suspend fun insertCategory(category: CategoryApiEntity) = withContext(Dispatchers.IO) {
        tryPerform { apiService.insertCategory(category).mapToApiResult() }
    }

    override suspend fun updateCategory(category: CategoryApiEntity) = withContext(Dispatchers.IO) {
        tryPerform { apiService.updateCategory(category).mapToApiResult() }
    }

    override suspend fun deleteCategory(id: Int) = withContext(Dispatchers.IO) {
        tryPerform { apiService.deleteCategory(IdBodyRequest(id)).mapToApiResult() }
    }

    private inline fun <T> tryPerform(request: () -> ApiResult<T>): ApiResult<T> {
        return try {
            request()
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

}