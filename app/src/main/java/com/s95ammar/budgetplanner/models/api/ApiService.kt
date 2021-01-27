package com.s95ammar.budgetplanner.models.api

import androidx.annotation.Nullable
import com.s95ammar.budgetplanner.models.api.requests.*
import com.s95ammar.budgetplanner.models.api.responses.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/register")
    suspend fun register(@Body userCredentials: UserCredentials): Response<TokenResponse>

    @GET("/auth/login")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): Response<TokenResponse>

    @GET("/auth/authenticate")
    suspend fun authenticate(): Response<String>

    @GET("/category/get")
    suspend fun getCategory(@Nullable @Query("id") id: Int?): Response<List<CategoryApiEntity>>

    @POST("/category/insert")
    suspend fun insertCategory(@Body request: CategoryUpsertApiRequest.Insertion): Response<CategoryApiEntity>

    @POST("/category/update")
    suspend fun updateCategory(@Body request: CategoryUpsertApiRequest.Update): Response<CategoryApiEntity>

    @POST("/category/delete")
    suspend fun deleteCategory(@Body id: IdBodyRequest): Response<Boolean>

    @GET("/period/get")
    suspend fun getPeriod(
        @Query("id") id: Int,
        @Query("includePeriodRecords") includePeriodRecords: Boolean,
        @Query("includeBudgetTransactions") includeBudgetTransactions: Boolean,
        @Query("includeSavings") includeSavings: Boolean
    ): Response<PeriodApiEntity>

    @GET("/period/getAll")
    suspend fun getAllUserPeriods(): Response<List<PeriodSimpleApiEntity>>

    @GET("/period/getInsertTemplate")
    suspend fun getPeriodInsertTemplate(): Response<PeriodApiEntity>

    @POST("/period/insert")
    suspend fun insertPeriod(@Body request: PeriodUpsertApiRequest.Insertion): Response<PeriodApiEntity>

    @POST("/period/update")
    suspend fun updatePeriod(@Body request: PeriodUpsertApiRequest.Update): Response<PeriodApiEntity>

    @POST("/period/delete")
    suspend fun deletePeriod(@Body id: IdBodyRequest): Response<Boolean>

    @GET("/period/budgetTransaction/get")
    suspend fun getBudgetTransaction(
        @Nullable @Query("id") id: Int?,
        @Nullable @Query("periodId") periodId: Int?
    ): Response<List<BudgetTransactionApiEntity>>

    @POST("/period/budgetTransaction/insert")
    suspend fun insertBudgetTransaction(@Body request: BudgetTransactionUpsertApiRequest.Insertion): Response<BudgetTransactionApiEntity>

    @POST("/period/budgetTransaction/update")
    suspend fun updateBudgetTransaction(@Body request: BudgetTransactionUpsertApiRequest.Update): Response<BudgetTransactionApiEntity>

    @POST("/period/budgetTransaction/delete")
    suspend fun deleteBudgetTransaction(@Body id: IdBodyRequest): Response<Boolean>

}