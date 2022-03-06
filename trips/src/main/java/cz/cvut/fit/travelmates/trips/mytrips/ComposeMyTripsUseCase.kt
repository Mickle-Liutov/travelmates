package cz.cvut.fit.travelmates.trips.mytrips

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.user.UserService
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTrip
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsItem
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitle
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitleType

class ComposeMyTripsUseCase(
    private val tripsRepository: TripsRepository,
    private val userService: UserService
) {

    suspend fun composeMyTrips(): List<MyTripsItem> {
        val trips = tripsRepository.getMyTrips()
        val user = userService.getUser().toBody()
        val ownerTrips = trips.filter { it.owner.name == user.name } /*TODO Change to email*/
        val requestTrips = emptyList<Trip>()
        val memberTrips = trips - ownerTrips /*TODO Change*/
        val subLists = listOf(
            MyTripsSubtitleType.OWNER to ownerTrips,
            MyTripsSubtitleType.REQUEST to requestTrips,
            MyTripsSubtitleType.MEMBER to memberTrips
        )
        return buildList {
            subLists.forEach { (type, items) ->
                if (items.isNotEmpty()) {
                    add(MyTripsSubtitle(type))
                }
                addAll(items.map {
                    MyTrip(
                        it.description,
                        it.id,
                        it.location,
                        it.owner,
                        it.requirements,
                        it.state,
                        it.suggestedTime,
                        it.title
                    )
                })
            }
        }
    }

}