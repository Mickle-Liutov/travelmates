package cz.cvut.fit.travelmates.location.pick

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.location.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickLocationViewModel @Inject constructor() : ViewModel() {

    //Set result to return back
    private val _eventSetResult = SingleLiveEvent<Location>()
    val eventSetResult = _eventSetResult.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Last set location
    private var lastLocation: LatLng? = null

    fun onCameraMoved(newPosition: LatLng) {
        lastLocation = newPosition
    }

    fun onPickPressed() {
        lastLocation?.let {
            //Setting result to return and navigating back
            _eventSetResult.value = Location(lat = it.latitude, lon = it.longitude)
            _eventNavigateBack.call()
        }
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }
}