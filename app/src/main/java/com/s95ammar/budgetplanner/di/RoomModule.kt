package com.s95ammar.budgetplanner.di

import android.content.Context
import androidx.room.Room
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabaseInstance(@ApplicationContext applicationContext: Context): BudgetPlannerDb {
        return Room.databaseBuilder(applicationContext, BudgetPlannerDb::class.java, BudgetPlannerDbConfig.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePeriodDao(db: BudgetPlannerDb) = db.periodDao

    @Singleton
    @Provides
    fun provideCategoryDao(db: BudgetPlannerDb) = db.categoryDao

    @Singleton
    @Provides
    fun providePeriodicCategoryDao(db: BudgetPlannerDb) = db.periodicCategoryDao

    @Singleton
    @Provides
    fun provideBudgetTransactionDao(db: BudgetPlannerDb) = db.budgetTransactionDao

}
