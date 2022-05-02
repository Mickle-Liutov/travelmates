package cz.cvut.fit.travelmates.trips.mytrips

import androidx.lifecycle.*
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTrip
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MyTripsViewModel @Inject constructor(
    private val composeMyTrips: ComposeMyTripsUseCase
) : ViewModel(), DefaultLifecycleObserver {

    //My trips items to show
    private val _trips = MutableLiveData<List<MyTripsItem>>()
    val trips = _trips.immutable()

    //ViewState for loading list of my trips
    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData()

    //Navigate to trip details
    private val _eventNavigateDetails = SingleLiveEvent<Long>()
    val eventNavigateDetails = _eventNavigateDetails.immutable()

    //Navigate to create trip screen
    private val _eventNavigateCreateTrip = SingleLiveEvent<Unit>()
    val eventNavigateCreateTrip = _eventNavigateCreateTrip.immutable()

    override fun onResume(owner: LifecycleOwner) {
        loadTrips()
    }

    fun onRetryPressed() = loadTrips()

    fun onAddTripPressed() {
        _eventNavigateCreateTrip.call()
    }

    private fun loadTrips() {
        viewState.value = ViewState.LOADING
        viewModelScope.launchCatching(execute = {
            val loadedTrips = composeMyTrips.invoke()
            _trips.value = loadedTrips
            viewState.value = ViewState.CONTENT
        }, catch = {
            viewState.value = ViewState.ERROR
        })
    }

    fun onTripPressed(trip: MyTrip) {
        _eventNavigateDetails.value = trip.id
    }

}