package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.models.BaseEntityMapper

data class LatLngEntity(
    val lat: Double,
    val lng: Double,
) {
    object EntityMapper : BaseEntityMapper<LatLng, LatLngEntity> {
        override fun toEntity(domainObj: LatLng?): LatLngEntity? {
            return domainObj?.let { LatLngEntity(it.latitude, it.longitude) }
        }

        override fun fromEntity(entity: LatLngEntity?): LatLng? {
            return entity?.let { LatLng(it.lat, it.lng) }
        }
    }
}