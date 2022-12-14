package cz.cvut.fit.travelmates.trips.tripdetails.models

/**
 * State of a join request
 */
enum class JoinRequestState {
    NOT_SENT, SENT, REJECTED_CAN_RESEND, REJECTED_CANT_RESEND
}