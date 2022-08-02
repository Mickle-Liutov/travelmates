package cz.cvut.fit.travelmates.authapi

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Reusable
    fun provideAuthRepository(): AuthRepository = AuthRepository()
}