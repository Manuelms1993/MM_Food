package com.example.mmfood.data.input.validation

import com.example.mmfood.domain.models.MenuPlan

data class MenuPlanFactoryResult(
    val menuPlan: MenuPlan?,
    val warnings: List<String>,
)

interface MenuPlanFactory {
    fun create(rawJson: String, sourceName: String): MenuPlanFactoryResult
}
