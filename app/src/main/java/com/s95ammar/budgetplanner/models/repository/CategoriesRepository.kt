package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.api.parseResponse
import com.s95ammar.budgetplanner.models.api.requests.CategoryUpsertApiRequest
import com.s95ammar.budgetplanner.models.api.requests.IdBodyRequest
import com.s95ammar.budgetplanner.models.datasource.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSource
import com.s95ammar.budgetplanner.util.flowOnIo
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    fun deleteCategory(id: Int) = flowOnIo {
        remoteDataSource.deleteCategory(IdBodyRequest(id))
            .parseResponse()
    }

    fun getAllUserCategories() = flowOnIo {
        remoteDataSource.getCategory(id = null)
            .parseResponse()
    }

    fun getCategory(id: Int) = flowOnIo {
        remoteDataSource.getCategory(id)
            .parseResponse()
            .map { it.singleOrNull() }
    }

    fun insertCategory(request: CategoryUpsertApiRequest.Insertion) = flowOnIo {
        remoteDataSource.insertCategory(request)
            .parseResponse()
    }

    fun updateCategory(request: CategoryUpsertApiRequest.Update) = flowOnIo {
        remoteDataSource.updateCategory(request)
            .parseResponse()
    }

}