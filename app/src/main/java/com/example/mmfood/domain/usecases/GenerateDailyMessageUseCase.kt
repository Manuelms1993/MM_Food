package com.example.mmfood.domain.usecases

import com.example.mmfood.data.repositories.AppendResult
import com.example.mmfood.data.repositories.ConversationDataSource
import com.example.mmfood.data.repositories.MenuCatalogDataSource
import com.example.mmfood.domain.MessageBuilder
import com.example.mmfood.domain.models.DailyMessage
import com.example.mmfood.domain.models.MessageSource
import com.example.mmfood.domain.models.MessageStatus
import java.time.LocalDate

class GenerateDailyMessageUseCase(
    private val menuCatalogDataSource: MenuCatalogDataSource,
    private val conversationRepository: ConversationDataSource,
    private val messageBuilder: MessageBuilder,
) {
    suspend operator fun invoke(
        date: LocalDate,
        source: MessageSource,
    ): GenerateDailyMessageResult {
        if (conversationRepository.hasMessageForDate(date)) {
            return GenerateDailyMessageResult(
                status = DailyGenerationStatus.SKIPPED_EXISTING,
                dailyMessage = null,
            )
        }

        val selection = menuCatalogDataSource.getCatalog().selectionForDate(date)
        val dailyMessage = messageBuilder.build(selection)
        return when (
            conversationRepository.appendMessageForDate(
                dailyMessage = dailyMessage,
                source = source,
                status = MessageStatus.GENERATED,
                createdAt = System.currentTimeMillis(),
            )
        ) {
            is AppendResult.Inserted -> GenerateDailyMessageResult(
                status = DailyGenerationStatus.GENERATED,
                dailyMessage = dailyMessage,
            )

            AppendResult.SkippedExisting -> GenerateDailyMessageResult(
                status = DailyGenerationStatus.SKIPPED_EXISTING,
                dailyMessage = null,
            )
        }
    }

    suspend fun markError(date: LocalDate) {
        conversationRepository.markError(date)
    }
}

enum class DailyGenerationStatus {
    GENERATED,
    SKIPPED_EXISTING,
}

data class GenerateDailyMessageResult(
    val status: DailyGenerationStatus,
    val dailyMessage: DailyMessage?,
)
