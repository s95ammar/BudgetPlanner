package com.s95ammar.budgetplanner.models

interface BaseEntityMapper<Domain, Entity> {

    fun toEntity(domainObj: Domain?): Entity? = null

    fun fromEntity(entity: Entity?): Domain? = null
}