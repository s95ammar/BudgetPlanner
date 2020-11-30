package com.s95ammar.budgetplanner.models.api

import androidx.annotation.Nullable
import com.s95ammar.budgetplanner.models.api.requests.IdBodyRequest
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.common.CategoryApiEntity
import com.s95ammar.budgetplanner.models.api.common.PeriodApiEntity
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
    suspend fun insertCategory(@Body category: CategoryApiEntity): Response<CategoryApiEntity>

    @POST("/category/update")
    suspend fun updateCategory(@Body category: CategoryApiEntity): Response<CategoryApiEntity>

    @POST("/category/delete")
    suspend fun deleteCategory(@Body id: IdBodyRequest): Response<Boolean>

    @GET("/period/get")
    suspend fun getPeriod(@Nullable @Query("id") id: Int?): Response<List<PeriodApiEntity>>

    @POST("/period/insert")
    suspend fun insertPeriod(@Body period: PeriodApiEntity): Response<PeriodApiEntity>

    @POST("/period/update")
    suspend fun updatePeriod(@Body period: PeriodApiEntity): Response<PeriodApiEntity>

    @POST("/period/delete")
    suspend fun deletePeriod(@Body id: IdBodyRequest): Response<Boolean>

}