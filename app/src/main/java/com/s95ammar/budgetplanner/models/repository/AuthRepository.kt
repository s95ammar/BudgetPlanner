package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.parseResponse
import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.models.api.responses.errors.EmptyResponseError
import com.s95ammar.budgetplanner.models.datasource.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSource
import com.s95ammar.budgetplanner.util.flowOnIo
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    fun tokenExists(): Boolean {
        return !localDataSource.loadAuthToken().isNullOrEmpty()
    }

    suspend fun login(email: String, password: String) = flowOnIo {
        remoteDataSource.login(email, password)
            .parseResponse()
            .map { tokenResponse ->
                if (tokenResponse.token == null) throw EmptyResponseError()
                localDataSource.saveAuthToken(tokenResponse.token)
            }
    }

    suspend fun register(userCredentials: UserCredentials) = flowOnIo {
        remoteDataSource.register(userCredentials)
            .parseResponse()
            .map { tokenResponse ->
                if (tokenResponse.token == null) throw EmptyResponseError()
                localDataSource.saveAuthToken(tokenResponse.token)
            }
    }

}