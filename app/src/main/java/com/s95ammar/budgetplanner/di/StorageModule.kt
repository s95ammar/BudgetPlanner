package com.s95ammar.budgetplanner.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.s95ammar.budgetplanner.flavor.FlavorConfig
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDb
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig
import com.s95ammar.budgetplanner.models.datasource.local.prefs.BudgetPlannerPrefsConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Singleton
    @Provides
    fun provideDatabaseInstance(
        @ApplicationContext applicationContext: Context,
        flavorConfig: FlavorConfig
    ): BudgetPlannerDb {
        return with(flavorConfig) {
            Room.databaseBuilder(applicationContext, BudgetPlannerDb::class.java, BudgetPlannerDbConfig.DB_NAME)
                .configureDbPrepopulation()
                .fallbackToDestructiveMigration()
                .build()
        }
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

    @Singleton
    @Provides
    fun provideJoinDao(db: BudgetPlannerDb) = db.joinDao

    @Singleton
    @Provides
    fun provideCurrencyDao(db: BudgetPlannerDb) = db.currencyDao

    @Singleton
    @Provides
    fun provideCurrencySharedPrefs(
        @ApplicationContext applicationContext: Context
    ) : SharedPreferences {
        return applicationContext.getSharedPreferences(BudgetPlannerPrefsConfig.CURRENCY_PREFS, Context.MODE_PRIVATE)
    }
}
