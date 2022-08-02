package cz.cvut.fit.travelmates.mainapi

import com.squareup.moshi.Moshi
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.mainapi.moshi.LocalDateTimeAdapter
import cz.cvut.fit.travelmates.mainapi.posts.PostsService
import cz.cvut.fit.travelmates.mainapi.requests.RequestsService
import cz.cvut.fit.travelmates.mainapi.trips.TripsService
import cz.cvut.fit.travelmates.mainapi.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainApiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(LocalDateTimeAdapter)
            .build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(authRepository: AuthRepository): AuthInterceptor =
        AuthInterceptor(authRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient, apiConfig: ApiConfig): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(apiConfig.baseUrl)
            .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideTripsService(retrofit: Retrofit): TripsService =
        retrofit.create(TripsService::class.java)

    @Provides
    @Singleton
    fun provideRequestsService(retrofit: Retrofit): RequestsService =
        retrofit.create(RequestsService::class.java)

    @Provides
    @Singleton
    fun providePostsService(retrofit: Retrofit): PostsService =
        retrofit.create(PostsService::class.java)

}