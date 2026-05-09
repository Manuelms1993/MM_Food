package com.example.mmfood.data.repositories

import com.example.mmfood.domain.models.ConversationMessage
import com.example.mmfood.domain.models.DailyMessage
import com.example.mmfood.domain.models.MessageSource
import com.example.mmfood.domain.models.MessageStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ConversationDataSource {
    fun getConversationFlow(): Flow<List<ConversationMessage>>

    suspend fun appendMessageForDate(
        dailyMessage: DailyMessage,
        source: MessageSource,
        status: MessageStatus,
        createdAt: Long,
    ): AppendResult

    suspend fun hasMessageForDate(date: LocalDate): Boolean

    suspend fun markError(date: LocalDate)

    suspend fun deleteBetween(fromDate: LocalDate, toDate: LocalDate)

    suspend fun deleteAll()
}
