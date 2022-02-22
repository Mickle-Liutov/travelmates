package cz.cvut.fit.travelmates.posts

import cz.cvut.fit.travelmates.mainapi.posts.PostsService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PostsModule {

    @Provides
    @Reusable
    fun providePostsRepository(postsService: PostsService): PostsRepository =
        PostsRepository(postsService)

}