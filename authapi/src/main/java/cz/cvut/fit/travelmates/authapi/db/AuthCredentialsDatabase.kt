package cz.cvut.fit.travelmates.authapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.cvut.fit.travelmates.authapi.models.AuthCredentials

@Database(entities = [AuthCredentials::class], version = 1)
abstract class AuthCredentialsDatabase : RoomDatabase() {
    abstract fun authCredentialsDao(): AuthCredentialsDao

    companion object {
        const val DB_NAME = "auth_credentials_db"
    }
}