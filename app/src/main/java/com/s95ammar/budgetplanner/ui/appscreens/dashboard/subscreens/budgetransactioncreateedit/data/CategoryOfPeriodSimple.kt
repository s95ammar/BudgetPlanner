package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.CategoryOfPeriodSimpleJoinEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryOfPeriodSimple(
    val id: Int,
    val currencyCode: String,
    val categoryName: String
) : Parcelable {

    object Mapper : BaseEntityMapper<CategoryOfPeriodSimple, CategoryOfPeriodSimpleJoinEntity> {
        override fun fromEntity(entity: CategoryOfPeriodSimpleJoinEntity?): CategoryOfPeriodSimple? {
            return entity?.let {
                CategoryOfPeriodSimple(
                    id = it.id,
                    currencyCode = it.currencyCode,
                    categoryName = it.categoryName
                )
            }
        }
    }
}
