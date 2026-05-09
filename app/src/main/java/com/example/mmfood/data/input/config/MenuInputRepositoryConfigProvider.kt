package com.example.mmfood.data.input.config

import com.example.mmfood.BuildConfig

interface MenuInputRepositoryConfigProvider {
    fun get(): MenuInputRepositoryConfig
}

class BuildConfigMenuInputRepositoryConfigProvider : MenuInputRepositoryConfigProvider {
    override fun get(): MenuInputRepositoryConfig = MenuInputRepositoryConfig(
        inputsRepositoryTreeUrl = BuildConfig.INPUTS_REPOSITORY_TREE_URL,
        expectedFileNames = setOf("comidas.json", "cenas.json"),
    )
}
