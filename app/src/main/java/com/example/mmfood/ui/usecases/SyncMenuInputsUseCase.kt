package com.example.mmfood.ui.usecases

import com.example.mmfood.data.repositories.MenuCatalogDataSource
import com.example.mmfood.ui.HomeOperationMessageFormatter

class SyncMenuInputsUseCase(
    private val menuCatalogDataSource: MenuCatalogDataSource,
    private val messageFormatter: HomeOperationMessageFormatter,
) {
    suspend operator fun invoke(): String {
        val result = menuCatalogDataSource.syncRemoteChanges()
        return messageFormatter.format(result)
    }
}
