package cz.cvut.fit.travelmates.trips.join

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.mainapi.trips.models.NewJoinRequestDto
import cz.cvut.fit.travelmates.trips.TripsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class JoinTripViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripsRepository: TripsRepository
) : ViewModel() {

    private val args = JoinTripFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _equipment = MutableStateFlow(args.trip.requirements.map {
        ProvidedRequirement(it.id, it.name, false)
    })
    val equipment = _equipment.asLiveData()

    val typedContact = MutableStateFlow("")
    val typedMessage = MutableStateFlow("")

    private val _eventRequestSent = SingleLiveEvent<Unit>()
    val eventRequestSent = _eventRequestSent.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    fun onItemChecked(itemId: Long, isChecked: Boolean) {
        val oldEquipment = _equipment.value
        val newEquipment = oldEquipment.map {
            if (it.id == itemId) {
                it.copy(isChecked = isChecked)
            } else {
                it
            }
        }
        _equipment.value = newEquipment
    }

    fun onSendPressed() {
        val contact = typedContact.value
        val message = typedMessage.value
        val providedEquipment = _equipment.value.filter { it.isChecked }.map {
            it.id
        }
        val newJoinRequest = NewJoinRequestDto(message, contact, providedEquipment)
        viewModelScope.launchCatching(execute = {
            tripsRepository.sendJoinRequest(args.trip.id, newJoinRequest)
            _eventRequestSent.call()
            _eventNavigateBack.call()
        }, catch = {
            Timber.e(it)
            //TODO Handle
        })
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}