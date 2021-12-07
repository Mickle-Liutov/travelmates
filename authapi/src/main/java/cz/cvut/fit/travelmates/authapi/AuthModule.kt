package cz.cvut.fit.travelmates.authapi

import android.content.Context
import androidx.room.Room
import cz.cvut.fit.travelmates.authapi.data.CredentialsDataSource
import cz.cvut.fit.travelmates.authapi.db.AuthCredentialsDao
import cz.cvut.fit.travelmates.authapi.db.AuthCredentialsDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthCredentialsDatabase(@ApplicationContext context: Context): AuthCredentialsDatabase =
        Room.databaseBuilder(
            context,
            AuthCredentialsDatabase::class.java,
            AuthCredentialsDatabase.DB_NAME
        ).build()

    @Provides
    fun provideAuthCredentialsDao(authCredentialsDatabase: AuthCredentialsDatabase): AuthCredentialsDao =
        authCredentialsDatabase.authCredentialsDao()

    @Provides
    @Reusable
    fun provideCredentialsDataSource(authCredentialsDao: AuthCredentialsDao): CredentialsDataSource =
        CredentialsDataSource(authCredentialsDao)
}