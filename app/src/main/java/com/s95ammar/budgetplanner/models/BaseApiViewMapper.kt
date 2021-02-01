package com.s95ammar.budgetplanner.models

interface BaseApiViewMapper<ViewEntity, ApiEntity> {

    fun toApiEntity(viewEntity: ViewEntity?): ApiEntity? = null

    fun toViewEntity(apiEntity: ApiEntity?): ViewEntity? = null
}