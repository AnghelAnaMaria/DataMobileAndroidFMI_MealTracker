package cst.unibucfmi2026.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cst.unibucfmi2026.utils.FtcFoodAPI
import cst.unibucfmi2026.utils.SearchResultFood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoodSearchState(
    val isLoading: Boolean = false,
    val results: List<SearchResultFood> = emptyList(),
    val errorMessage: String? = null
)

class FoodTestViewModel : ViewModel() {
    private val api = FtcFoodAPI()
    private val _searchState = MutableStateFlow(FoodSearchState())
    val searchState = _searchState.asStateFlow()

    fun search(query: String) {
        _searchState.value = FoodSearchState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = api.searchFoods(query, pageSize = 10)
                _searchState.value = FoodSearchState(results = result.foods)
            } catch (e: Exception) {
                _searchState.value = FoodSearchState(errorMessage = e.message)
            }
        }
    }
}
