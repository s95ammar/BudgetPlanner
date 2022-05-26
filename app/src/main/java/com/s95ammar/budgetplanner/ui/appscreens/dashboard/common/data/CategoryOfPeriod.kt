package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryOfPeriodEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodJoinEntity
import com.s95ammar.budgetplanner.util.INVALID
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryOfPeriod(
    val id: Int,
    val periodId: Int,
    val categoryId: Int,
    val categoryName: String,
    var estimate: Double?,
    val currencyCode: String,
    val budgetTransactionsAmountSum: Double,
    val isSelected: Boolean
) : Parcelable {
    object JoinEntityMapper : BaseEntityMapper<CategoryOfPeriod, CategoryOfPeriodJoinEntity> {

        override fun fromEntity(entity: CategoryOfPeriodJoinEntity?): CategoryOfPeriod? {
            return entity?.let {
                CategoryOfPeriod(
                    it.categoryOfPeriodId,
                    it.periodId,
                    it.categoryId,
                    it.categoryName,
                    it.estimate,
                    it.currencyCode,
                    it.budgetTransactionsAmountSum,
                    isSelected = it.categoryOfPeriodId != Int.INVALID
                )
            }
        }
    }

    object EntityMapper : BaseEntityMapper<CategoryOfPeriod, CategoryOfPeriodEntity> {

        override fun toEntity(domainObj: CategoryOfPeriod?): CategoryOfPeriodEntity? {
            return domainObj?.let {
                CategoryOfPeriodEntity(
                    estimate = it.estimate,
                    categoryId = it.categoryId,
                    periodId = it.periodId,
                    currencyCode = it.currencyCode
                ).apply { id = it.id }
            }

        }
    }
}
