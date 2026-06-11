package cst.unibucfmi2026.utils

import android.content.Context
import android.content.SharedPreferences
import cst.unibucfmi2026.BuildConfig

class DeveloperSettingsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("developer_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FDC_API_KEY = "fdc_api_key"
        
        @Volatile
        private var INSTANCE: DeveloperSettingsManager? = null

        fun getInstance(context: Context): DeveloperSettingsManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DeveloperSettingsManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    var fdcApiKey: String
        get() {
            val defaultKey = if (BuildConfig.FDC_API_KEY.isBlank()) "DEMO_KEY" else BuildConfig.FDC_API_KEY
            return sharedPreferences.getString(KEY_FDC_API_KEY, defaultKey) ?: defaultKey
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_FDC_API_KEY, value).apply()
        }
    
    fun resetFdcApiKey() {
        sharedPreferences.edit().remove(KEY_FDC_API_KEY).apply()
    }
}
