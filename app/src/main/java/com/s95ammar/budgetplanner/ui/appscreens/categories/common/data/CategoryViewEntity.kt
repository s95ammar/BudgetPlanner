package com.s95ammar.budgetplanner.ui.appscreens.categories.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.CategoryApiEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryViewEntity(
    val id: Int,
    val name: String
): Parcelable {
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