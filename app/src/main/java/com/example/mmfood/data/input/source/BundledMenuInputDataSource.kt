package com.example.mmfood.data.input.source

import android.content.Context
import com.example.mmfood.data.input.RawMenuInput

class BundledMenuInputDataSource(
    private val context: Context,
) {
    fun loadInputs(expectedFileNames: Set<String>): List<RawMenuInput> {
        val rootFiles = context.assets.list("").orEmpty()
            .filter { it in expectedFileNames }
            .sorted()

        if (rootFiles.isNotEmpty()) {
            return rootFiles.mapNotNull(::readAssetFile)
        }

        return context.assets.list("inputs").orEmpty()
            .filter { it in expectedFileNames }
            .sorted()
            .mapNotNull { fileName -> readAssetFile(fileName, "inputs/$fileName") }
    }

    private fun readAssetFile(fileName: String, assetPath: String = fileName): RawMenuInput? = runCatching {
        RawMenuInput(
            fileName = fileName,
            rawJson = context.assets.open(assetPath).bufferedReader().use { it.readText() },
        )
    }.getOrNull()
}
