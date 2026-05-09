package com.example.mmfood.ui

import com.example.mmfood.data.input.MenuSyncResult
import com.example.mmfood.domain.usecases.PendingGenerationSummary

class HomeOperationMessageFormatter {
    fun format(summary: PendingGenerationSummary): String = buildString {
        append("Generados: ${summary.generatedCount}")
        if (summary.errors.isNotEmpty()) {
            append(" · Errores: ${summary.errors.size}")
        }
    }

    fun format(result: MenuSyncResult): String = buildString {
        append("Sincronizado")
        append(" · Nuevos: ${result.newCount}")
        append(" · Actualizados: ${result.updatedCount}")
        append(" · Sin cambios: ${result.unchangedCount}")
        if (result.removedCount > 0) {
            append(" · Eliminados: ${result.removedCount}")
        }
    }
}
