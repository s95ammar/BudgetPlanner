package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.CategoryApiEntity
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.util.NO_ITEM

object CategoryApiViewMapper : BaseApiViewMapper<CategoryViewEntity, CategoryApiEntity> {

    override fun toViewEntity(apiEntity: CategoryApiEntity): CategoryViewEntity {
        return CategoryViewEntity(
            id = apiEntity.id ?: Int.NO_ITEM,
            name = apiEntity.name.orEmpty()
        )
    }

}