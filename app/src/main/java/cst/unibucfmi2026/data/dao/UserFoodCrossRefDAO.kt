package cst.unibucfmi2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import cst.unibucfmi2026.data.entities.UserFoodCrossRef

@Dao
interface UserFoodCrossRefDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: UserFoodCrossRef)

    @Delete
    suspend fun delete(crossRef: UserFoodCrossRef)
}