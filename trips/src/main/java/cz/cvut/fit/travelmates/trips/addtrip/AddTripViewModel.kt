package cz.cvut.fit.travelmates.trips.addtrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Requirement
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.addtrip.requirements.AddItem
import cz.cvut.fit.travelmates.trips.addtrip.requirements.RequirementItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddTripViewModel @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModel() {

    private val _eventNavigateAddRequirement = SingleLiveEvent<Unit>()
    val eventNavigateAddRequirement = _eventNavigateAddRequirement.immutable()

    private val _eventShowTripCreated = SingleLiveEvent<Unit>()
    val eventShowTripCreated = _eventShowTripCreated.immutable()

    private val _eventNavigatePickLocation = SingleLiveEvent<Unit>()
    val eventNavigatePickLocation = _eventNavigatePickLocation.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedTitle = MutableStateFlow("")
    val typedDescription = MutableStateFlow("")

    val isCreateEnabled = MutableStateFlow(true)

    private val requirements = MutableStateFlow<List<RequirementItem>>(emptyList())
    val requirementsUi = requirements.map {
        it + AddItem
    }.asLiveData()

    private val _location = MutableStateFlow<Location?>(null)

    fun onAddRequirementPressed() {
        _eventNavigateAddRequirement.call()
    }

    fun onRequirementAdded(name: String) {
        requirements.value = requirements.value + RequirementItem(name)
    }

    fun onDeleteRequirementPressed(item: RequirementItem) {
        requirements.value = requirements.value - item
    }

    fun onCreatePressed() {
        val title = typedTitle.value
        val description = typedDescription.value
        val requirements = requirements.value.map { Requirement(it.name) }
        val location = _location.value ?: return
        //TODO Add fields ownerContact, suggestedDate
        val newTrip =
            NewTripDto(
                title,
                description,
                location,
                "Contact",
                requirements,
                "2021-02-07"
            )
        viewModelScope.launchCatching(execute = {
            tripsRepository.createTrip(newTrip)
            _eventShowTripCreated.call()
            _eventNavigateBack.call()
        }, catch = {
            Timber.d(it)
            //TODO Handle
        })
    }

    fun onPickLocationPressed() {
        _eventNavigatePickLocation.call()
    }

    fun onLocationPicked(location: Location) {
        _location.value = location
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}