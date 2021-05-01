package com.s95ammar.budgetplanner.models

interface BaseApiViewMapper<ViewEntity, ApiEntity> {

    fun toApiEntity(viewEntity: ViewEntity?): ApiEntity? = null

    fun fromEntity(apiEntity: ApiEntity?): ViewEntity? = null
}