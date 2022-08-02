package cz.cvut.fit.travelmates.trips.tripdetails.members

import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember

/**
 * Participant of a trip, which includes members and the owner
 */
data class TripParticipant(
    val tripMember: TripMember,
    val isOwner: Boolean
)