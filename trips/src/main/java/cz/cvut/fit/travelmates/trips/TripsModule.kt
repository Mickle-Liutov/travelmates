package cz.cvut.fit.travelmates.trips

import cz.cvut.fit.travelmates.mainapi.requests.RequestsService
import cz.cvut.fit.travelmates.mainapi.trips.TripsService
import cz.cvut.fit.travelmates.trips.request.RequestsRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TripsModule {

    @Provides
    @Reusable
    fun provideTripsRepository(tripsService: TripsService): TripsRepository =
        TripsRepository(tripsService)

    @Provides
    @Reusable
    fun provideRequestsRepository(requestsService: RequestsService): RequestsRepository =
        RequestsRepository(requestsService)

}