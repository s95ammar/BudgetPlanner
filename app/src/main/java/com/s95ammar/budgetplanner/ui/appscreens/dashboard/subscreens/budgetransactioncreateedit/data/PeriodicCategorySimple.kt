package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategorySimpleJoinEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodicCategorySimple(
    val id: Int,
    val currencyCode: String,
    val categoryName: String
) : Parcelable {

    object Mapper : BaseEntityMapper<PeriodicCategorySimple, PeriodicCategorySimpleJoinEntity> {
        override fun fromEntity(entity: PeriodicCategorySimpleJoinEntity?): PeriodicCategorySimple? {
            return entity?.let {
                PeriodicCategorySimple(
                    id = it.id,
                    currencyCode = it.currencyCode,
                    categoryName = it.categoryName
                )
            }
        }
    }
}
