package com.s95ammar.budgetplanner.ui.appscreens.categories.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.CategoryApiEntity

data class CategoryViewEntity(
    val id: Int,
    val name: String
) {
    object ApiMapper : BaseApiViewMapper<CategoryViewEntity, CategoryApiEntity> {

        override fun toViewEntity(apiEntity: CategoryApiEntity?): CategoryViewEntity? {
            if (apiEntity?.id == null || apiEntity.name == null) return null

            return CategoryViewEntity(
                id = apiEntity.id,
                name = apiEntity.name.orEmpty()
            )
        }
    }
}