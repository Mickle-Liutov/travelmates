package cz.cvut.fit.travelmates.authapi.db

import androidx.room.Dao
import androidx.room.Query
import cz.cvut.fit.travelmates.authapi.models.AuthCredentials
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthCredentialsDao {

    @Query("SELECT * FROM auth_credentials LIMIT 1")
    fun getAuthCredentialsFlow(): Flow<AuthCredentials?>

}