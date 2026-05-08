package cst.unibucfmi2026.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = ARG_ID)
    val id: String,  // Firebase UID
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
) {
    companion object {
        const val ARG_ID = "id"
    }
}
