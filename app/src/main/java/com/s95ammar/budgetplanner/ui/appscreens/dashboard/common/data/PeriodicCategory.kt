package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INVALID
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodicCategory(
    val id: Int,
    val periodId: Int,
    val categoryId: Int,
    val categoryName: String,
    var estimate: Double?,
    val currencyCode: String,
    val budgetTransactionsAmountSum: Double,
    val isSelected: Boolean
) : Parcelable {
    object JoinEntityMapper : BaseEntityMapper<PeriodicCategory, PeriodicCategoryJoinEntity> {

        override fun fromEntity(entity: PeriodicCategoryJoinEntity?): PeriodicCategory? {
            return entity?.let {
                PeriodicCategory(
                    it.periodicCategoryId,
                    it.periodId,
                    it.categoryId,
                    it.categoryName,
                    it.estimate,
                    it.currencyCode,
                    it.budgetTransactionsAmountSum,
                    isSelected = it.periodicCategoryId != Int.INVALID
                )
            }
        }
    }

    object EntityMapper : BaseEntityMapper<PeriodicCategory, PeriodicCategoryEntity> {

        override fun toEntity(domainObj: PeriodicCategory?): PeriodicCategoryEntity? {
            return domainObj?.let {
                PeriodicCategoryEntity(
                    estimate = it.estimate,
                    categoryId = it.categoryId,
                    periodId = it.periodId,
                    currencyCode = it.currencyCode
                ).apply { id = it.id }
            }

        }
    }
}
