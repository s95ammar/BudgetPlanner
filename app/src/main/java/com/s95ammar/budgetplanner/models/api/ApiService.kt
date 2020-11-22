package com.s95ammar.budgetplanner.models.api

import com.s95ammar.budgetplanner.models.api.requests.TokenResponse
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/register")
    suspend fun register(@Body userCredentials: UserCredentials): Response<TokenResponse>

    @GET("/auth/login")
    suspend fun login(@Body userCredentials: UserCredentials): Response<TokenResponse>

    @GET("/auth/authenticate")
    suspend fun authenticate(): Response<String>
}