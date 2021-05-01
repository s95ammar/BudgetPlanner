package com.s95ammar.budgetplanner.di

import android.content.Context
import com.s95ammar.budgetplanner.util.currentLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun provideLocale(@ApplicationContext applicationContext: Context): Locale {
            return applicationContext.currentLocale
        }
    }

}