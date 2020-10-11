package com.s95ammar.budgetplanner.di

import android.content.Context
import android.content.SharedPreferences
import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManager
import com.s95ammar.budgetplanner.models.sharedprefs.SharedPrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun provideSharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
            return applicationContext.getSharedPreferences(SharedPrefsManagerImpl.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    @Binds
    abstract fun bindSharedPreferencesManager(sharedPreferences: SharedPrefsManagerImpl): SharedPrefsManager
}