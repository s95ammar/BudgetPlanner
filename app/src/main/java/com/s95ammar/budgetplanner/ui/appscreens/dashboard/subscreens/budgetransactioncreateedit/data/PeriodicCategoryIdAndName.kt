package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryIdAndNameJoinEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodicCategoryIdAndName(val periodicCategoryId: Int, val categoryName: String) : Parcelable {

    object Mapper : BaseEntityMapper<PeriodicCategoryIdAndName, PeriodicCategoryIdAndNameJoinEntity> {
        override fun fromEntity(entity: PeriodicCategoryIdAndNameJoinEntity?): PeriodicCategoryIdAndName? {
            return entity?.let {
                PeriodicCategoryIdAndName(it.periodicCategoryId, it.categoryName)
            }
        }
    }

}
