package com.s95ammar.budgetplanner.ui.appscreens.categories.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String
): Parcelable {
    object Mapper : BaseEntityMapper<Category, CategoryEntity> {

        override fun fromEntity(entity: CategoryEntity?): Category? {
            return entity?.let {
                Category(it.id, it.name)
            }
        }

    }
}