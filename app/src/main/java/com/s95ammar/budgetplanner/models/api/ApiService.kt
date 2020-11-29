package com.s95ammar.budgetplanner.models.api

import androidx.annotation.Nullable
import com.s95ammar.budgetplanner.models.api.responses.TokenResponse
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.CategoryApiEntity
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
}