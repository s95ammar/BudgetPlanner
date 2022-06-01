package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.ui.common.simplemenu.SimpleMenuItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodSimple(
    val id: Int,
    val name: String
) : Parcelable, SimpleMenuItem {

    override fun getMenuItemTitle(): String {
        return name
    }

    override fun getMenuItemId(): Int {
        return id
    }

    object Mapper : BaseEntityMapper<PeriodSimple, PeriodEntity> {

        override fun fromEntity(entity: PeriodEntity?): PeriodSimple? {
            return entity?.let {
                PeriodSimple(it.id, it.name)
            }
        }
    }
}