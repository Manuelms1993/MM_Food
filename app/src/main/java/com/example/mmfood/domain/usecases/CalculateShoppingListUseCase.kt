package com.example.mmfood.domain.usecases

import com.example.mmfood.data.repositories.MenuCatalogDataSource
import com.example.mmfood.domain.models.MenuOption
import com.example.mmfood.domain.models.ShoppingList
import com.example.mmfood.domain.models.ShoppingListItem
import java.time.DayOfWeek
import java.time.LocalDate

class CalculateShoppingListUseCase(
    private val menuCatalogDataSource: MenuCatalogDataSource,
) {
    suspend operator fun invoke(
        today: LocalDate = LocalDate.now(),
    ): ShoppingList {
        val catalog = menuCatalogDataSource.getCatalog()
        val targetSaturday = resolveTargetSaturday(today)
        val ingredientCounts = linkedMapOf<String, Int>()

        generateSequence(today) { current ->
            current.plusDays(1).takeUnless { it.isAfter(targetSaturday) }
        }.forEach { date ->
            val selection = catalog.selectionForDate(date)
            selection.lunchOptions
                .plus(selection.dinnerOptions)
                .flatMap(MenuOption::ingredients)
                .forEach { ingredient ->
                    val key = ingredient.trim()
                    if (key.isNotBlank()) {
                        ingredientCounts[key] = (ingredientCounts[key] ?: 0) + 1
                    }
                }
        }

        return ShoppingList(
            fromDate = today,
            toDate = targetSaturday,
            items = ingredientCounts.entries
                .sortedBy { it.key.lowercase() }
                .map { ShoppingListItem(it.key, it.value) },
        )
    }

    internal fun resolveTargetSaturday(today: LocalDate): LocalDate {
        val saturdayThisWeek = today.with(DayOfWeek.SATURDAY)
        return if (today.dayOfWeek.value >= DayOfWeek.WEDNESDAY.value) {
            saturdayThisWeek.plusWeeks(1)
        } else {
            saturdayThisWeek
        }
    }
}
