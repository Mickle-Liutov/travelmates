package cz.cvut.fit.travelmates.explore

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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModel() {

    private val _eventNavigateTripDetails = SingleLiveEvent<Long>()
    val eventNavigateTripDetails = _eventNavigateTripDetails.immutable()

    private val _exploreTrips = MutableStateFlow<List<Trip>>(emptyList())
    val exploreTrips = _exploreTrips.asLiveData()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData() //TODO Observe

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewState.value = ViewState.LOADING
        viewModelScope.launchCatching(execute = {
            val exploreTrips = tripsRepository.getExploreTrips()
            _exploreTrips.value = exploreTrips
            viewState.value = ViewState.CONTENT
        }, catch = {
            viewState.value = ViewState.ERROR
        })
    }

    fun onTripPressed(tripId: Long) {
        _eventNavigateTripDetails.value = tripId
    }

}