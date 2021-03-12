package com.moon.coinavenue.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AvenueViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AvenueViewModel() as T
    }
}