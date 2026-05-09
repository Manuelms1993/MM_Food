package com.example.mmfood.ui.usecases

import com.example.mmfood.data.repositories.ConversationDataSource
import com.example.mmfood.domain.models.ConversationMessage
import kotlinx.coroutines.flow.Flow

class ObserveConversationUseCase(
    private val conversationDataSource: ConversationDataSource,
) {
    operator fun invoke(): Flow<List<ConversationMessage>> = conversationDataSource.getConversationFlow()
}
