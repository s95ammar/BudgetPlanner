package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.PeriodicCategoryApiEntity

data class PeriodicCategoryViewEntity(
    val categoryName: String,
    val categoryId: Int,
    var max: Int?,
    val amount: Int,
    val isSelected: Boolean
) {
    object ApiMapper : BaseApiViewMapper<PeriodicCategoryViewEntity, PeriodicCategoryApiEntity> {

        override fun toViewEntity(apiEntity: PeriodicCategoryApiEntity?): PeriodicCategoryViewEntity? {
            if (
                apiEntity?.categoryName == null
                || apiEntity.categoryId == null
                || apiEntity.amount == null
                || apiEntity.isSelected == null
            ) return null

            return PeriodicCategoryViewEntity(
                categoryName = apiEntity.categoryName,
                categoryId = apiEntity.categoryId,
                max = apiEntity.max,
                amount = apiEntity.amount,
                isSelected = apiEntity.isSelected
            )
        }

    }
}