package cst.unibucfmi2026.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cst.unibucfmi2026.data.AppDatabase
import cst.unibucfmi2026.data.entities.FoodEntity
import cst.unibucfmi2026.data.entities.UserEntity
import cst.unibucfmi2026.data.entities.UserFoodCrossRef
import cst.unibucfmi2026.utils.DeveloperSettingsManager
import cst.unibucfmi2026.utils.FtcFoodAPI
import cst.unibucfmi2026.utils.SearchResultFood
import cst.unibucfmi2026.utils.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FoodSearchState(
    val isLoading: Boolean = false,
    val results: List<SearchResultFood> = emptyList(),
    val errorMessage: String? = null
)

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val userDao = db.userDao()
    private val foodDao = db.foodDao()
    private val crossRefDao = db.userFoodCrossRefDao()
    private val devSettings = DeveloperSettingsManager.getInstance(application)
    private val _searchState = MutableStateFlow(FoodSearchState())
    val searchState = _searchState.asStateFlow()

    private fun getApi() = FtcFoodAPI(apiKey = devSettings.fdcApiKey)

    private fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid

    val userFoods = flow<List<FoodEntity>> {
        val uid = currentUid() ?: return@flow
        if (userDao.getById(uid) == null) {
            userDao.insert(UserEntity(id = uid, firstName = "", lastName = ""))
        }
        emitAll(userDao.getFoodsForUser(uid).map { it?.foods ?: emptyList() })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun search(query: String) {
        _searchState.value = FoodSearchState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = getApi().searchFoods(query, pageSize = 10)
                _searchState.value = FoodSearchState(results = result.foods)
            } catch (e: Exception) {
                _searchState.value = FoodSearchState(errorMessage = e.message)
            }
        }
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
