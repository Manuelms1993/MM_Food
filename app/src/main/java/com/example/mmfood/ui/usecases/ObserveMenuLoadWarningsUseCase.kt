package com.example.mmfood.ui.usecases

import com.example.mmfood.data.repositories.MenuCatalogDataSource
import kotlinx.coroutines.flow.StateFlow

class ObserveMenuLoadWarningsUseCase(
    private val menuCatalogDataSource: MenuCatalogDataSource,
) {
    operator fun invoke(): StateFlow<String?> = menuCatalogDataSource.loadWarningMessage
}
