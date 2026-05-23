package cst.unibucfmi2026.data

import android.content.Context
import cst.unibucfmi2026.viewmodel.SortConfig
import cst.unibucfmi2026.viewmodel.SortField
import cst.unibucfmi2026.viewmodel.SortOrder

class UserPreferencesRepository(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getSortConfig(): SortConfig {
        val fieldName = prefs.getString(KEY_SORT_FIELD, SortField.NAME.name)
        val orderName = prefs.getString(KEY_SORT_ORDER, SortOrder.ASC.name)
        return SortConfig(
            field = SortField.entries.firstOrNull { it.name == fieldName } ?: SortField.NAME,
            order = SortOrder.entries.firstOrNull { it.name == orderName } ?: SortOrder.ASC
        )
    }

    fun saveSortConfig(config: SortConfig) {
        prefs.edit()
            .putString(KEY_SORT_FIELD, config.field.name)
            .putString(KEY_SORT_ORDER, config.order.name)
            .apply()
    }

    fun getDarkMode(): Boolean = prefs.getBoolean(KEY_DARK_MODE, false)

    fun saveDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    companion object {
        private const val KEY_SORT_FIELD = "sort_field"
        private const val KEY_SORT_ORDER = "sort_order"
        private const val KEY_DARK_MODE = "dark_mode"
    }
}