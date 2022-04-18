package cz.cvut.fit.travelmates.trips.tripdetails.members

import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember

data class TripParticipant(
    val tripMember: TripMember,
    val isOwner: Boolean
)