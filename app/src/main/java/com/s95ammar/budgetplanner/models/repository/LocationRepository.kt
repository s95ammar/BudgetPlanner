package com.s95ammar.budgetplanner.models.repository

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    val locale: Locale,
    @ApplicationContext val context: Context
) {

    suspend fun getAddressByCoordinates(latitude: Double, longitude: Double): String? {
        return withContext(Dispatchers.IO) {
            runCatching {
                Geocoder(context, locale)
                    .getFromLocation(latitude, longitude, 1)
                    ?.firstOrNull()
                    ?.getAddressLine(0)
            }.getOrNull()
        }
    }

}