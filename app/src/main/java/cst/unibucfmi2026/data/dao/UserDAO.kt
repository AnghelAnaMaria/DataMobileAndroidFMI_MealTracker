package cst.unibucfmi2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cst.unibucfmi2026.data.entities.UserEntity
import cst.unibucfmi2026.data.entities.UserWithFoods
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE ${UserEntity.ARG_ID} = :uid")
    suspend fun getById(uid: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM users WHERE ${UserEntity.ARG_ID} = :uid")
    fun getFoodsForUser(uid: String): Flow<UserWithFoods?>

    @Delete
    suspend fun delete(user: UserEntity)
}
