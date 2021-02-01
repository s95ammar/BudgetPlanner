package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.PeriodRecordApiEntity

data class PeriodRecordViewEntity(
    val categoryName: String,
    val categoryId: Int,
    var max: Int?,
    val amount: Int,
    val isSelected: Boolean
) {
    object ApiMapper : BaseApiViewMapper<PeriodRecordViewEntity, PeriodRecordApiEntity> {

        override fun toViewEntity(apiEntity: PeriodRecordApiEntity?): PeriodRecordViewEntity? {
            if (
                apiEntity?.categoryName == null
                || apiEntity.categoryId == null
                || apiEntity.amount == null
                || apiEntity.isSelected == null
            ) return null

            return PeriodRecordViewEntity(
                categoryName = apiEntity.categoryName,
                categoryId = apiEntity.categoryId,
                max = apiEntity.max,
                amount = apiEntity.amount,
                isSelected = apiEntity.isSelected
            )
        }

    }
}