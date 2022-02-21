package cz.cvut.fit.travelmates.images

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ImagesModule {

    @Provides
    @Reusable
    fun provideImagesRepository(): ImagesRepository = ImagesRepository()

}