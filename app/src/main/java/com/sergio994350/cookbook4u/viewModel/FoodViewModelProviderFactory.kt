package com.sergio994350.cookbook4u.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergio994350.cookbook4u.CookBookApplication
import com.sergio994350.cookbook4u.repository.FoodRepository

class FoodViewModelProviderFactory(
    val app: CookBookApplication,
    val foodRepository: FoodRepository
) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FoodViewModel(app, foodRepository) as T
    }
}