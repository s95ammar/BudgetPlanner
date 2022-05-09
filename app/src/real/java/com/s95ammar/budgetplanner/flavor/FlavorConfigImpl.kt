package com.s95ammar.budgetplanner.flavor

import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import javax.inject.Inject

class FlavorConfigImpl @Inject constructor() : FlavorConfig {

    override fun <T : RoomDatabase> Builder<T>.configureDbPrepopulation(): Builder<T> = this
}
