package cz.cvut.fit.travelmates.auth.shared

import cz.cvut.fit.travelmates.auth.login.LoginUseCase
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.mainapi.user.UserService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Reusable
    internal fun provideFormValidator(): FormValidator = FormValidator()

    @Provides
    internal fun provideLoginUseCase(
        authRepository: AuthRepository,
        userService: UserService
    ): LoginUseCase = LoginUseCase(authRepository, userService)

}