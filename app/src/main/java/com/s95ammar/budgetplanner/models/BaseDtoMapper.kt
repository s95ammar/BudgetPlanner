package com.s95ammar.budgetplanner.models

interface BaseDtoMapper<Domain, Dto> {

    fun toDto(domainObj: Domain?): Dto? = null

    fun fromDto(dto: Dto?): Domain? = null
}