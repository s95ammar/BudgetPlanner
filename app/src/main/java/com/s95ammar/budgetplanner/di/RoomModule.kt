package com.s95ammar.budgetplanner.di

import android.app.Application
import androidx.room.Room
import com.s95ammar.budgetplanner.models.persistence.BudgetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabaseInstance(application: Application): BudgetsDatabase {
        return Room.databaseBuilder(application.applicationContext, BudgetsDatabase::class.java, BudgetsDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBudgetDao(db: BudgetsDatabase) = db.getBudgetDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: BudgetsDatabase) = db.getCategoryDao()

    @Singleton
    @Provides
    fun provideBudgetTransactionDao(db: BudgetsDatabase) = db.getBudgetTransactionDao()

}