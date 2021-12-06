package cz.cvut.fit.travelmates.authapi.data

import cz.cvut.fit.travelmates.authapi.db.AuthCredentialsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CredentialsDataSource(private val authCredentialsDao: AuthCredentialsDao) {

    fun hasValidCredentials(): Flow<Boolean> {
        return authCredentialsDao.getAuthCredentialsFlow().map {
            it != null
        }
    }

}