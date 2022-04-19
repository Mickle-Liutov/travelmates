package cz.cvut.fit.travelmates.trips.addtrip.contact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = ContactFragmentArgs.fromSavedStateHandle(savedStateHandle)

    //Show message that trip was created
    private val _eventShowTripCreated = SingleLiveEvent<Unit>()
    val eventShowTripCreated = _eventShowTripCreated.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Navigate to main
    private val _eventNavigateMain = SingleLiveEvent<Unit>()
    val eventNavigateMain = _eventNavigateMain.immutable()

    //Show error while creating trip
    private val _eventError = SingleLiveEvent<Unit>()
    val eventError = _eventError.immutable()

    //Contact, synchronized with input field
    val typedContact = MutableStateFlow("")

    //Whether create button is enabled or not
    val isCreateEnabled = typedContact.map {
        it.isNotBlank()
    }.asLiveData()

    //ViewState for create trip action
    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    fun onCreatePressed() {
        //Create new trip object
        val newTrip = with(args.partialTrip) {
            NewTripDto(
                title,
                description,
                location,
                typedContact.value,
                requirements,
                suggestedDate
            )
        }
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            tripsRepository.createTrip(newTrip)
            //Show message and navigate to main if created successfully
            _eventShowTripCreated.call()
            _eventNavigateMain.call()
        }, catch = {
            _eventError.call()
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}