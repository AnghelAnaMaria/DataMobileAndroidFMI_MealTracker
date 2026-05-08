package cst.unibucfmi2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cst.unibucfmi2026.data.entities.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity) : Long // Room returns Long type

    @Delete
    suspend fun delete(food: FoodEntity)

    @Query("SELECT * FROM foods WHERE ${FoodEntity.ARG_ID} = :id")
    suspend fun getById(id: Int) : FoodEntity?

    @Query("SELECT * FROM foods")
    fun getAll() : Flow<List<FoodEntity>>

}