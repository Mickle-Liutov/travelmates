package cz.cvut.fit.travelmates.trips.tripdetails

import cz.cvut.fit.travelmates.mainapi.trips.models.RequestState
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.TripState
import cz.cvut.fit.travelmates.mainapi.trips.models.UserType
import cz.cvut.fit.travelmates.trips.tripdetails.models.JoinRequestState
import cz.cvut.fit.travelmates.trips.tripdetails.models.TripDetailsState

/**
 * Maps trip into its state for Trip details screen
 */
class TripDetailsStateMapper {

    /**
     * Get trip state of a given trip
     */
    fun getTripState(trip: Trip): TripDetailsState {
        if (trip.state == TripState.FINISHED) {
            return TripDetailsState.Finished
        }
        return when (trip.userType) {
            UserType.GUEST -> TripDetailsState.Guest(
                //Choose correct state of join request
                when (trip.currentUserRequest?.state) {
                    RequestState.PENDING -> JoinRequestState.SENT
                    RequestState.REJECTED_ALLOW_RESEND -> JoinRequestState.REJECTED_CAN_RESEND
                    RequestState.REJECTED_NO_RESEND -> JoinRequestState.REJECTED_CANT_RESEND
                    null -> JoinRequestState.NOT_SENT
                }
            )
            UserType.MEMBER -> TripDetailsState.Member
            UserType.OWNER -> when (trip.state) {
                TripState.GATHERING -> TripDetailsState.OwnerGathering
                else -> TripDetailsState.OwnerGathered
            }
        }
    }
}