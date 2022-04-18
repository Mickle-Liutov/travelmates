package cz.cvut.fit.travelmates.trips.mytrips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _trips = MutableLiveData<List<MyTripsItem>>()
    val trips = _trips.immutable()

    private val viewState = MutableStateFlow(ViewState.LOADING)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()
    val errorVisible = viewState.map { it == ViewState.ERROR }.asLiveData()

    private val _eventNavigateAdd = SingleLiveEvent<Unit>()
    val eventNavigateAdd = _eventNavigateAdd.immutable()

    private val _eventNavigateDetails = SingleLiveEvent<Long>()
    val eventNavigateDetails = _eventNavigateDetails.immutable()

    init {
        loadTrips()
    }

    fun onRetryPressed() = loadTrips()

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

    fun onAddPressed() {
        _eventNavigateAdd.call()
    }

    fun onTripPressed(trip: MyTrip) {
        _eventNavigateDetails.value = trip.id
    }

}