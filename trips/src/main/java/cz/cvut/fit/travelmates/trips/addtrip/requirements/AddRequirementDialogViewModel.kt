package cz.cvut.fit.travelmates.trips.addtrip.requirements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AddRequirementDialogViewModel @Inject constructor() : ViewModel() {

    //Set argument that will be returned as result
    private val _eventSetArgument = SingleLiveEvent<String>()
    val eventSetArgument = _eventSetArgument.immutable()

    //Navigate back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Name, synchronized with input field
    val typedName = MutableStateFlow("")

    //Whether add button is enabled or not
    val isAddEnabled = typedName.map { it.isNotBlank() }.asLiveData()

    fun onAddPressed() {
        val name = typedName.value
        _eventSetArgument.value = name
        _eventNavigateBack.call()
    }
}