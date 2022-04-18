package cz.cvut.fit.travelmates.trips.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val searchTrips: SearchTripsUseCase
) : ViewModel() {

    private val _eventNavigateTripDetails = SingleLiveEvent<Long>()
    val eventNavigateTripDetails = _eventNavigateTripDetails.immutable()

    private val exploreTrips = MutableStateFlow<List<Trip>>(emptyList())
    val searchTerm = MutableStateFlow("")
    val filteredTrips = combine(exploreTrips, searchTerm) { trips, term ->
        searchTrips.invoke(trips, term)
    }.asLiveData()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData()

    init {
        loadTrips()
    }

    fun onRetryPressed() = loadTrips()

    private fun loadTrips() {
        viewState.value = ViewState.LOADING
        viewModelScope.launchCatching(execute = {
            val trips = tripsRepository.getExploreTrips()
            exploreTrips.value = trips
            viewState.value = ViewState.CONTENT
        }, catch = {
            viewState.value = ViewState.ERROR
        })
    }

    fun onTripPressed(tripId: Long) {
        _eventNavigateTripDetails.value = tripId
    }

}