package com.s95ammar.budgetplanner.di

import com.s95ammar.budgetplanner.flavor.FlavorConfig
import com.s95ammar.budgetplanner.flavor.FlavorConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FlavorModule {

    @Binds
    abstract fun bindFlavorConfig(flavorConfigImpl: FlavorConfigImpl): FlavorConfig
}