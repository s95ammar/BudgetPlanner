package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.CategoryApiEntity
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity

object CategoryApiViewMapper : BaseApiViewMapper<CategoryViewEntity, CategoryApiEntity> {

    override fun toViewEntity(apiEntity: CategoryApiEntity?): CategoryViewEntity? {
        if (apiEntity?.id == null || apiEntity.name == null) return null

        return CategoryViewEntity(
            id = apiEntity.id,
            name = apiEntity.name.orEmpty()
        )
    }

}