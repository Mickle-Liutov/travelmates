package cz.cvut.fit.travelmates.trips.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripMemberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = TripMemberFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val member = args.tripMember
    val userImage = liveData { emit(member.picture) }
    val userName = liveData { emit(member.name) }
    val userContact = liveData { emit(member.contact) }

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}