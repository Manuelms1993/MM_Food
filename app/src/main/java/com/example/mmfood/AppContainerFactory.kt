package com.example.mmfood

import android.app.Application
import androidx.room.Room
import com.example.mmfood.data.db.AppDatabase
import com.example.mmfood.data.input.MenuPlanParser
import com.example.mmfood.data.input.config.BuildConfigMenuInputRepositoryConfigProvider
import com.example.mmfood.data.input.source.BundledMenuInputDataSource
import com.example.mmfood.data.input.source.GithubMenuInputDataSource
import com.example.mmfood.data.input.source.MenuInputCacheStore
import com.example.mmfood.data.input.source.MenuInputLoader
import com.example.mmfood.data.input.source.MenuInputSyncService
import com.example.mmfood.data.input.validation.MenuCatalogValidator
import com.example.mmfood.data.input.validation.MenuPlanValidator
import com.example.mmfood.data.repositories.ConversationRepository
import com.example.mmfood.data.repositories.MenuCatalogRepository
import com.example.mmfood.domain.MessageBuilder
import com.example.mmfood.domain.usecases.CalculateShoppingListUseCase
import com.example.mmfood.domain.usecases.GenerateDailyMessageUseCase
import com.example.mmfood.domain.usecases.GeneratePendingMessagesUseCase
import com.example.mmfood.ui.HomeOperationMessageFormatter
import com.example.mmfood.ui.usecases.GeneratePendingMenuUseCase
import com.example.mmfood.ui.usecases.LoadMenuCatalogUseCase
import com.example.mmfood.ui.usecases.ObserveConversationUseCase
import com.example.mmfood.ui.usecases.ObserveMenuLoadWarningsUseCase
import com.example.mmfood.ui.usecases.SyncMenuInputsUseCase

class AppContainerFactory(
    private val application: Application,
) {
    fun create(): AppContainer {
        val database = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "mm-food.db",
        ).build()

        val menuCatalogDataSource = createMenuCatalogDataSource()
        val conversationDataSource = ConversationRepository(database.conversationMessageDao())
        val generateDailyMessageUseCase = GenerateDailyMessageUseCase(
            menuCatalogDataSource = menuCatalogDataSource,
            conversationRepository = conversationDataSource,
            messageBuilder = MessageBuilder(),
        )
        val generatePendingMessagesUseCase = GeneratePendingMessagesUseCase(
            conversationRepository = conversationDataSource,
            menuCatalogDataSource = menuCatalogDataSource,
            generateDailyMessageUseCase = generateDailyMessageUseCase,
        )
        val messageFormatter = HomeOperationMessageFormatter()

        return AppContainer(
            observeConversationUseCase = ObserveConversationUseCase(conversationDataSource),
            loadMenuCatalogUseCase = LoadMenuCatalogUseCase(menuCatalogDataSource),
            observeMenuLoadWarningsUseCase = ObserveMenuLoadWarningsUseCase(menuCatalogDataSource),
            generatePendingMenuUseCase = GeneratePendingMenuUseCase(
                menuCatalogDataSource = menuCatalogDataSource,
                generatePendingMessagesUseCase = generatePendingMessagesUseCase,
                messageFormatter = messageFormatter,
            ),
            syncMenuInputsUseCase = SyncMenuInputsUseCase(
                menuCatalogDataSource = menuCatalogDataSource,
                messageFormatter = messageFormatter,
            ),
            calculateShoppingListUseCase = CalculateShoppingListUseCase(menuCatalogDataSource),
            menuCatalogDataSource = menuCatalogDataSource,
            conversationDataSource = conversationDataSource,
        )
    }

    private fun createMenuCatalogDataSource(): MenuCatalogRepository {
        val config = BuildConfigMenuInputRepositoryConfigProvider().get()
        val cacheStore = MenuInputCacheStore(application)
        val inputLoader = MenuInputLoader(
            bundledDataSource = BundledMenuInputDataSource(application),
            cacheStore = cacheStore,
            expectedFileNames = config.expectedFileNames,
        )
        val inputSyncService = MenuInputSyncService(
            remoteDataSource = GithubMenuInputDataSource(config),
            cacheStore = cacheStore,
        )
        return MenuCatalogRepository(
            menuInputLoader = inputLoader,
            menuInputSyncService = inputSyncService,
            menuCatalogValidator = MenuCatalogValidator(
                menuPlanFactory = MenuPlanParser(),
                menuPlanValidator = MenuPlanValidator(),
            ),
        )
    }
}
