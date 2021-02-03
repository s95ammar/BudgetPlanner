package com.s95ammar.budgetplanner.models.api.responses

data class PeriodSimpleApiEntity(
    val id: Int?,
    val name: String?,
    val max: Int?,
    val creationUnixMs: Long?,
    val userId: Int?
)