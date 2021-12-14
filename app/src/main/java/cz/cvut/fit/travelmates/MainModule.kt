package cz.cvut.fit.travelmates

import android.content.Context
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Reusable
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider =
        ResourcesProvider(context)

}