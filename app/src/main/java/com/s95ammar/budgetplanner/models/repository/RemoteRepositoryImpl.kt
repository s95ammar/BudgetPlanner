package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.ApiService
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) : RemoteRepository {

    override suspend fun register(userCredentials: UserCredentials) = withContext(Dispatchers.IO) { apiService.register(userCredentials) }
    override suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) { apiService.login(email, password) }
    override suspend fun authenticate() = withContext(Dispatchers.IO) { apiService.authenticate() }

}