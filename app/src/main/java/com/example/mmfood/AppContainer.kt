package com.example.mmfood

import com.example.mmfood.data.repositories.ConversationDataSource
import com.example.mmfood.data.repositories.MenuCatalogDataSource
import com.example.mmfood.domain.usecases.CalculateShoppingListUseCase
import com.example.mmfood.ui.usecases.GeneratePendingMenuUseCase
import com.example.mmfood.ui.usecases.LoadMenuCatalogUseCase
import com.example.mmfood.ui.usecases.ObserveConversationUseCase
import com.example.mmfood.ui.usecases.ObserveMenuLoadWarningsUseCase
import com.example.mmfood.ui.usecases.SyncMenuInputsUseCase

data class AppContainer(
    val observeConversationUseCase: ObserveConversationUseCase,
    val loadMenuCatalogUseCase: LoadMenuCatalogUseCase,
    val observeMenuLoadWarningsUseCase: ObserveMenuLoadWarningsUseCase,
    val generatePendingMenuUseCase: GeneratePendingMenuUseCase,
    val syncMenuInputsUseCase: SyncMenuInputsUseCase,
    val calculateShoppingListUseCase: CalculateShoppingListUseCase,
    val menuCatalogDataSource: MenuCatalogDataSource,
    val conversationDataSource: ConversationDataSource,
)
