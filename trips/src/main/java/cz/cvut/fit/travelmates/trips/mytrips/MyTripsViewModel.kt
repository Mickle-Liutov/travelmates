package cz.cvut.fit.travelmates.trips.mytrips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTripsViewModel @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModel() {

    private val _trips = MutableLiveData<List<Trip>>()
    val trips = _trips.immutable()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            val loadedTrips = tripsRepository.getMyTrips()
            _trips.value = loadedTrips
        }
    }

}