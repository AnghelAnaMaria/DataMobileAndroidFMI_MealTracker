package cst.unibucfmi2026.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cst.unibucfmi2026.data.AppDatabase
import cst.unibucfmi2026.data.entities.FoodEntity
import cst.unibucfmi2026.data.entities.UserEntity
import cst.unibucfmi2026.data.entities.UserFoodCrossRef
import cst.unibucfmi2026.utils.FoodItemDetail
import cst.unibucfmi2026.utils.FtcFoodAPI
import cst.unibucfmi2026.utils.SearchResultFood
import cst.unibucfmi2026.utils.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortField(val label: String) {
    NAME("Name"), CALORIES("Calories"), PROTEIN("Protein")
}

enum class SortOrder { ASC, DESC }

data class SortConfig(
    val field: SortField = SortField.NAME,
    val order: SortOrder = SortOrder.ASC
)

data class FoodSearchState(
    val isLoading: Boolean = false,
    val results: List<SearchResultFood> = emptyList(),
    val errorMessage: String? = null
)

data class FoodDetailState(
    val isLoading: Boolean = false,
    val detail: FoodItemDetail? = null,
    val errorMessage: String? = null
)

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val db = AppDatabase.getInstance(application)
    private val userDao = db.userDao()
    private val foodDao = db.foodDao()
    private val crossRefDao = db.userFoodCrossRefDao()
    private val api = FtcFoodAPI()
    private val _searchState = MutableStateFlow(FoodSearchState())
    val searchState = _searchState.asStateFlow()

    private val _foodDetailState = MutableStateFlow(FoodDetailState())
    val foodDetailState = _foodDetailState.asStateFlow()

    private val _sortConfig = MutableStateFlow(SortConfig())
    val sortConfig = _sortConfig.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid

    val userFoods = flow<List<FoodEntity>> {
        val uid = currentUid() ?: return@flow
        if (userDao.getById(uid) == null) {
            userDao.insert(UserEntity(id = uid, firstName = "", lastName = ""))
        }
        emitAll(userDao.getFoodsForUser(uid).map { it?.foods ?: emptyList() })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPages = userFoods.map { foods ->
        if (foods.isEmpty()) 1 else (foods.size + PAGE_SIZE - 1) / PAGE_SIZE
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val pagedFoods = combine(userFoods, _sortConfig, _currentPage) { foods, sort, page ->
        val sorted = when (sort.field) {
            SortField.NAME -> if (sort.order == SortOrder.ASC)
                foods.sortedBy { it.description.lowercase() }
            else
                foods.sortedByDescending { it.description.lowercase() }
            SortField.CALORIES -> if (sort.order == SortOrder.ASC)
                foods.sortedBy { it.calories ?: 0f }
            else
                foods.sortedByDescending { it.calories ?: 0f }
            SortField.PROTEIN -> if (sort.order == SortOrder.ASC)
                foods.sortedBy { it.protein ?: 0f }
            else
                foods.sortedByDescending { it.protein ?: 0f }
        }
        val maxPage = if (foods.isEmpty()) 0 else (foods.size - 1) / PAGE_SIZE
        sorted.drop(page.coerceAtMost(maxPage) * PAGE_SIZE).take(PAGE_SIZE)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSortConfig(config: SortConfig) {
        _sortConfig.value = config
        _currentPage.value = 0
    }

    fun nextPage() { _currentPage.value++ }
    fun prevPage() { if (_currentPage.value > 0) _currentPage.value-- }

    fun deleteFood(foodId: Int) {
        val uid = currentUid() ?: return
        viewModelScope.launch {
            crossRefDao.delete(UserFoodCrossRef(userId = uid, foodId = foodId))
        }
    }

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

    fun fetchFoodDetail(fdcId: Int) {
        _foodDetailState.value = FoodDetailState(isLoading = true)
        viewModelScope.launch {
            try {
                val detail = api.getFood(fdcId)
                _foodDetailState.value = FoodDetailState(detail = detail)
            } catch (e: Exception) {
                _foodDetailState.value = FoodDetailState(errorMessage = e.message)
            }
        }
    }

    fun clearFoodDetail() {
        _foodDetailState.value = FoodDetailState()
    }

    fun addFood(food: SearchResultFood, onSuccess: () -> Unit) {
        val uid = currentUid() ?: return
        viewModelScope.launch {
            val foodEntity = food.toEntity()
            foodDao.insert(foodEntity)
            crossRefDao.insert(UserFoodCrossRef(userId = uid, foodId = foodEntity.id))
            onSuccess()
        }
    }
}
