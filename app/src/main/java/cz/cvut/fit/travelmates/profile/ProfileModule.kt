package cz.cvut.fit.travelmates.profile

import cz.cvut.fit.travelmates.mainapi.user.UserService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Reusable
    fun provideUserRepository(userService: UserService): UserRepository =
        UserRepository(userService)

}