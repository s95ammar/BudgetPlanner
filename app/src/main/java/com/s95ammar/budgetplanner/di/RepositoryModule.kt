package com.s95ammar.budgetplanner.di

import com.s95ammar.budgetplanner.models.datasource.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.LocalDataSourceImpl
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSource
import com.s95ammar.budgetplanner.models.datasource.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindLocalRepository(localRepository: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindremoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}