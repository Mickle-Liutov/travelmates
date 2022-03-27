package cz.cvut.fit.travelmates.trips.tripdetails.models

/**
 * State of the trip details screen, based on trip state and user's membership
 */
sealed class TripDetailsState() {
    open val finishGatheringVisible = false
    open val finishTripVisible = false
    open val joinRequestsVisible = false
    open val picturesSectionVisible = false
    open val canOpenMemberDetails = false
    open val requestToJoinVisible = false
    open val requestSentVisible = false
    open val requestRejected = false

    object OwnerGathering : TripDetailsState() {
        override val finishGatheringVisible = true
        override val canOpenMemberDetails = true
        override val joinRequestsVisible = true
    }

    object OwnerGathered : TripDetailsState() {
        override val finishTripVisible = true
        override val canOpenMemberDetails = true
    }

    object Member : TripDetailsState() {
        override val canOpenMemberDetails = true
    }

    data class Guest(val joinRequestState: JoinRequestState) : TripDetailsState() {
        override val requestToJoinVisible = joinRequestState in listOf(
            JoinRequestState.NOT_SENT,
            JoinRequestState.REJECTED_CAN_RESEND
        )
        override val requestSentVisible = joinRequestState == JoinRequestState.SENT
        override val requestRejected = joinRequestState in listOf(
            JoinRequestState.REJECTED_CAN_RESEND,
            JoinRequestState.REJECTED_CANT_RESEND
        )
    }

    object Finished : TripDetailsState() {
        override val picturesSectionVisible = true
        override val canOpenMemberDetails = true
    }

}


