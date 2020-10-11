package com.s95ammar.budgetplanner.di

import com.s95ammar.budgetplanner.models.repository.LocalRepositoryImpl
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(localRepository: LocalRepositoryImpl): LocalRepository
}