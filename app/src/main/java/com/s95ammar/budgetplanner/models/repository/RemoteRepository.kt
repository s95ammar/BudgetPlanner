package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.common.CategoryApiEntity
import com.s95ammar.budgetplanner.models.api.common.PeriodApiEntity
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse

interface RemoteRepository {

    suspend fun register(userCredentials: UserCredentials): ApiResult<TokenResponse>
    suspend fun login(email: String, password: String): ApiResult<TokenResponse>
    suspend fun authenticate(): ApiResult<String>

    suspend fun getAllUserCategories(): ApiResult<List<CategoryApiEntity>>
    suspend fun getCategory(id: Int): ApiResult<List<CategoryApiEntity>>
    suspend fun insertCategory(category: CategoryApiEntity): ApiResult<CategoryApiEntity>
    suspend fun updateCategory(category: CategoryApiEntity): ApiResult<CategoryApiEntity>
    suspend fun deleteCategory(id: Int): ApiResult<Boolean>

    suspend fun getAllUserPeriods(): ApiResult<List<PeriodApiEntity>>
    suspend fun getPeriod(id: Int): ApiResult<List<PeriodApiEntity>>
    suspend fun insertPeriod(period: PeriodApiEntity): ApiResult<PeriodApiEntity>
    suspend fun updatePeriod(period: PeriodApiEntity): ApiResult<PeriodApiEntity>
    suspend fun deletePeriod(id: Int): ApiResult<Boolean>
}