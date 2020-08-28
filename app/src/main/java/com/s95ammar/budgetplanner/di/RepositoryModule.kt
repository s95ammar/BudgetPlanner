package com.s95ammar.budgetplanner.di

import com.s95ammar.budgetplanner.models.repository.PersistenceRepository
import com.s95ammar.budgetplanner.models.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(persistenceRepository: PersistenceRepository): Repository
}