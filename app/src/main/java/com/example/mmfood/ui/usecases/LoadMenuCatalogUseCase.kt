package com.example.mmfood.ui.usecases

import com.example.mmfood.data.repositories.MenuCatalogDataSource
import com.example.mmfood.domain.models.MenuCatalog

class LoadMenuCatalogUseCase(
    private val menuCatalogDataSource: MenuCatalogDataSource,
) {
    suspend operator fun invoke(): MenuCatalog = menuCatalogDataSource.getCatalog()
}
