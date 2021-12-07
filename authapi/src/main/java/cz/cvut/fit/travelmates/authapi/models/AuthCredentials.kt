package cz.cvut.fit.travelmates.authapi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_credentials")
data class AuthCredentials(
    @ColumnInfo(name = "authToken")
    val authToken: String,
    @ColumnInfo(name = "refreshToken")
    val refreshToken: String,
    @ColumnInfo(name = "email")
    val email: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 0
)