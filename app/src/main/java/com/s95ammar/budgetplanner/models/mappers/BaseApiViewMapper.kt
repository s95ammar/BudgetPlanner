package com.s95ammar.budgetplanner.models.mappers

interface BaseApiViewMapper<ViewEntity, ApiEntity> {

    fun toApiEntity(viewEntity: ViewEntity): ApiEntity {
        throw NotImplementedError()
    }

    fun toViewEntity(apiEntity: ApiEntity): ViewEntity {
        throw NotImplementedError()
    }
}