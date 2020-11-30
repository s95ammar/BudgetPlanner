package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.ApiResponseMapper.mapToApiResult
import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.common.CategoryApiEntity
import com.s95ammar.budgetplanner.models.api.common.PeriodApiEntity
import com.s95ammar.budgetplanner.models.api.requests.IdBodyRequest
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) : RemoteRepository {

    override suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse> = apiRequest {
        apiService.register(userCredentials)
    }

    override suspend fun login(email: String, password: String) = apiRequest {
        apiService.login(email, password)
    }

    override suspend fun authenticate() = apiRequest {
        apiService.authenticate()
    }

    override suspend fun getAllUserCategories() = apiRequest {
        apiService.getCategory(id = null)
    }

    override suspend fun getCategory(id: Int) = apiRequest {
        apiService.getCategory(id)
    }

    override suspend fun insertCategory(category: CategoryApiEntity) = apiRequest {
        apiService.insertCategory(category)
    }

    override suspend fun updateCategory(category: CategoryApiEntity) = apiRequest {
        apiService.updateCategory(category)
    }

    override suspend fun deleteCategory(id: Int) = apiRequest {
        apiService.deleteCategory(IdBodyRequest(id))
    }

    override suspend fun getAllUserPeriods() = apiRequest {
        apiService.getPeriod(id = null)
    }

    override suspend fun getPeriod(id: Int) = apiRequest {
        apiService.getPeriod(id)
    }

    override suspend fun insertPeriod(period: PeriodApiEntity) = apiRequest {
        apiService.insertPeriod(period)
    }

    override suspend fun updatePeriod(period: PeriodApiEntity) = apiRequest {
        apiService.updatePeriod(period)
    }

    override suspend fun deletePeriod(id: Int) = apiRequest {
        apiService.deletePeriod(IdBodyRequest(id))
    }

    private suspend fun <T> apiRequest(request: suspend () -> Response<T>): ApiResult<T> = withContext(Dispatchers.IO) {
        try {
            request().mapToApiResult()
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

}