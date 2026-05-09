package com.example.mmfood.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mmfood.AppContainer
import com.example.mmfood.ui.screens.HomeScreen
import com.example.mmfood.ui.theme.MMFoodTheme

@Composable
fun MMFoodApp(
    container: AppContainer,
) {
    MMFoodTheme {
        val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(container))
        HomeScreen(viewModel = viewModel)
    }
}

