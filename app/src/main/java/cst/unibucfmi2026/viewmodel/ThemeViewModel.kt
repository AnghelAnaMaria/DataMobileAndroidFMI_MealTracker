package cst.unibucfmi2026.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cst.unibucfmi2026.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = UserPreferencesRepository(application)

    private val _isDarkTheme = MutableStateFlow(prefsRepo.getDarkMode())
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggle() {
        _isDarkTheme.value = !_isDarkTheme.value
        prefsRepo.saveDarkMode(_isDarkTheme.value)
    }
}