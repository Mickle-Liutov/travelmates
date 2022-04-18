package cz.cvut.fit.travelmates.trips.addtrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.Requirement
import cz.cvut.fit.travelmates.trips.addtrip.requirements.AddItem
import cz.cvut.fit.travelmates.trips.addtrip.requirements.RequirementItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTripViewModel @Inject constructor() : ViewModel() {

    private val _eventNavigateAddRequirement = SingleLiveEvent<Unit>()
    val eventNavigateAddRequirement = _eventNavigateAddRequirement.immutable()

    private val _eventNavigatePickLocation = SingleLiveEvent<Unit>()
    val eventNavigatePickLocation = _eventNavigatePickLocation.immutable()

    private val _eventPickDate = SingleLiveEvent<LocalDate>()
    val eventPickDate = _eventPickDate.immutable()

    private val _eventNavigateContactStep = SingleLiveEvent<PartialTrip>()
    val eventNavigateContactStep = _eventNavigateContactStep.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedTitle = MutableStateFlow("")
    val typedDescription = MutableStateFlow("")
    val suggestedDate = MutableStateFlow(LocalDate.now())

    private val requirements = MutableStateFlow<List<RequirementItem>>(emptyList())
    val requirementsUi = requirements.map {
        it + AddItem
    }.asLiveData()

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.filterNotNull().asLiveData()

    val isCreateEnabled =
        combine(_location, typedTitle, typedDescription) { location, title, description ->
            location != null && title.isNotBlank() && description.isNotBlank()
        }.asLiveData()

    fun onAddRequirementPressed() {
        _eventNavigateAddRequirement.call()
    }

    fun onRequirementAdded(name: String) {
        requirements.value =
            requirements.value + RequirementItem(UUID.randomUUID().toString(), name)
    }

    fun onDeleteRequirementPressed(item: RequirementItem) {
        requirements.value = requirements.value - item
    }

    fun onCreatePressed() {
        val title = typedTitle.value
        val description = typedDescription.value
        val requirements = requirements.value.map { Requirement(it.name) }
        val location = _location.value ?: return
        val partialTrip =
            PartialTrip(title, description, location, requirements, suggestedDate.value)
        _eventNavigateContactStep.value = partialTrip
    }

    fun onPickLocationPressed() {
        _eventNavigatePickLocation.call()
    }

    fun onLocationPicked(location: Location) {
        _location.value = location
    }

    fun onChangeDatePressed() {
        _eventPickDate.value = suggestedDate.value
    }

    fun onDateSet(newDate: LocalDate) {
        suggestedDate.value = newDate
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}