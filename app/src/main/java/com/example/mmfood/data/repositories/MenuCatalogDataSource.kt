package com.example.mmfood.data.repositories

import com.example.mmfood.data.input.MenuSyncResult
import com.example.mmfood.domain.models.MenuCatalog
import java.time.LocalDate
import kotlinx.coroutines.flow.StateFlow

interface MenuCatalogDataSource {
    val loadWarningMessage: StateFlow<String?>

    suspend fun getCatalog(): MenuCatalog

    suspend fun getEarliestStartDate(): LocalDate?

    suspend fun refreshLocalSource(): MenuCatalog

    suspend fun syncRemoteChanges(): MenuSyncResult
}
