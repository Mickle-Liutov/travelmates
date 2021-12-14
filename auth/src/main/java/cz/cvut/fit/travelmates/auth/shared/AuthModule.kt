package cz.cvut.fit.travelmates.auth.shared

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

}